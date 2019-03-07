package com.cyh.controller;

import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cyh.common.Constants;
import com.cyh.exception.MyException;
import com.cyh.pojo.MenuPermission;
import com.cyh.pojo.Role;
import com.cyh.service.MenuPermissionService;
import com.cyh.util.*;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cyh.pojo.User;
import com.cyh.service.UserService;
import redis.clients.jedis.Jedis;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuPermissionService menuPermissionService;

    // 登录验证
    @RequestMapping("CUser")
    public String CUser(HttpServletRequest request,User user, HttpSession session){
        Object sessionCheckCode = request.getSession().getAttribute("checkCode");
//        Object asdf = session.getAttribute("checkCode");
        if(user.getYzm() != null && !"".equals(user.getYzm()) && sessionCheckCode != null && !"".equals(sessionCheckCode)){
            if (user.getYzm().equalsIgnoreCase(sessionCheckCode.toString())) {
                //验证通过！
                System.out.println("验证码验证通过！");
            } else {
                session.setAttribute("message","验证码错误,请重试！");
                return "redirect:login";
            }
        }else{
            session.setAttribute("message","非法验证码,请重试！");
            return "redirect:login";
        }

        if(user.getName() == null || user.getPassword() == null){
            session.setAttribute("message","账号或密码错误,请重试！");
            return "redirect:login";
        }
        //密码加密后再去数据库进行匹配
        try {
            user.setPassword(EncryptionAndDecryptionUtil.getEncryptionPassword(user.getPassword()));
        } catch (MyException e) {
            e.printStackTrace();
            session.setAttribute("message","服务器内部错误！");
            return "redirect:login";
        }
        User loginuser = userService.CUser(user);

        if (loginuser == null) { // 如果数据库查找不到该用户说明用户名或者密码错误！
            session.setAttribute("message","账号或密码错误,请重试！");
            return "redirect:login";
        } else if(loginuser.getStruts().equals("0")){ //账号处于冻结状态
            session.setAttribute("message","此账号已冻结，请联系管理员！");
            return "redirect:login";
        }else {
            //获取系统登录用户的ip信息
            JSONObject IPInfo = GetUserIpUtil.getUserIpAddress(request);
            if(IPInfo != null && 0 == (int)IPInfo.get("code")){
                JSONObject info =  (JSONObject) IPInfo.get("data");
                // 存入用户的ip地址
                loginuser.setIp((String) info.get("ip"));
                //存入用户所在省
                loginuser.setUserRegion((String) info.get("region"));
                // 存入用户所在城市
                loginuser.setUserCity((String) info.get("city"));
            }else{
                //没有获取到登录用户的ip详细信息
                loginuser.setIp(IPInfo.get("ip").toString());
                loginuser.setUserRegion("未知省份");
                loginuser.setUserCity("未知城市");
            }

            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = new Date();

            String time = loginuser.getTime(); // 展示上次的登录时间
            // String all_time = loginuser.getAll_time(); //展示使用系统总时间
            // String last_time = loginuser.getLast_time(); //展示上次使用本系统时间

            // 如果上次登陆时间为空，说明是新用户 展示上次的登录时间 = 新用户
            if (StringUtils.isEmpty(time)) {
                time = "您是新用户！。。";
            }

            //根据用户id获取用户的有效角色的id
            List<Integer> roleIds = userService.getRolesListByUserId(loginuser.getId());
            if(null != roleIds && roleIds.size() != 0){
                // 菜单和权限集合
                List<MenuPermission> allMenusAndPermissions = menuPermissionService.getAllMenusAndPermissionsByRoleIds(roleIds);
                // 分离的菜单
                List<MenuPermission> menus = new ArrayList<>();
                // 分离的页面内权限
                List<MenuPermission> permissions = new ArrayList<>();

                for (MenuPermission mp : allMenusAndPermissions) {
                    if("menu".equals(mp.getType())){
                        menus.add(mp);
                    }else if("permission".equals(mp.getType())){
                        permissions.add(mp);
                    }
                }
                //变成菜单tree,然后存入session
                session.setAttribute("menuList", new Gson().toJson(MenuPermissionUtil.getMenuTree(menus)));
                //用户的授权 并存入session
                session.setAttribute("permissionList", permissions);
            }

            // 登录信息存入session
            session.setAttribute("user", loginuser);
            session.setAttribute("time", time.substring(0, time.length() - 2));

            //以秒为单位，即在没有活动4小时后，session将失效
            session.setMaxInactiveInterval(14400);

            //如果开启了聊天服务  就获取聊天服务器ip  以方便页面的WebSocket连接聊天服务器
            session.setAttribute("webSocketChatSwitch", Constants.webSocketChatSwitch);
            if(Constants.webSocketChatSwitch){  //再在session中存入聊天服务器所在地址
                session.setAttribute("webSocketChatAddress", Constants.webSocketChatAddress);
                //启动一个线程将当前登陆用户在线状态更新到redis中,并且再zk上创建一个有规律的子节点
                SessionListenerUtil slu = new SessionListenerUtil(loginuser.getId());
                Thread thread = new Thread(slu);
                thread.start();
            }

            loginuser.setTime(date.format(d)); // 将时间转化成有格式的字符串
            userService.updateUser(loginuser); // 将此次登录的时间记录
            return "redirect:main";
        }
    }

    // 调用计算器
    @ResponseBody
    @RequestMapping(value = "jisuanqi", produces = "text/html;charset=utf-8")
    public void jisuanqi() throws Exception {
        String osName = System.getProperty("os.name");
        System.out.println(osName);
        StringBuffer systempathBuff = new StringBuffer("");
        if (osName.indexOf("Windows") > -1) {
            systempathBuff.append("c:\\WINDOWS\\system32\\cmd.exe");
        }
        if (osName.indexOf("NT") > -1) {
            systempathBuff.append("c:\\WINDOWS\\command.exe");
        }
        String[] cmd = new String[2];
        cmd[0] = systempathBuff.toString();
        cmd[1] = "/c calc.exe";// 计算器的绝对路径
        System.out.println(cmd);
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 调用便签
    @ResponseBody
    @RequestMapping(value = "bianqian", produces = "text/html;charset=utf-8")
    public void bianqian() throws Exception {
        String path = "D:\\abc.exe";
        Runtime run = Runtime.getRuntime();
        try {
            // run.exec("cmd /k shutdown -s -t 3600");
            Process process = run.exec("cmd.exe /k start " + path);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 备份数据库 并发送邮件
    @RequestMapping("beifen")
    public ModelAndView beifen(HttpServletRequest request) throws Exception {
        Date d = new Date();
        SimpleDateFormat smf = new SimpleDateFormat("yyyyMMddHHmmSS");
        // 备份产生的文件名字
        String s1 = smf.format(d) + ".sql";
        Properties pro = System.getProperties();
        String osName = pro.getProperty("os.name");//获得当前操作系统的名称
        // 获取项目目录下的 beifen 这个文件夹
        String path = "";
        // 获取mysqldump所在路径
        String pathMysqldump = "";
        if("linux".equalsIgnoreCase(osName)){
            path = request.getSession().getServletContext().getRealPath("/")+"/beifen"; //linux环境下的路径
            pathMysqldump = request.getSession().getServletContext().getRealPath("/")+"/src"; //linux环境下的路径
        }else{
            path = request.getSession().getServletContext().getRealPath("/")+"beifen"; //windows
            pathMysqldump = request.getSession().getServletContext().getRealPath("/")+"src"; //windows
        }
        System.out.println("osName  是" + osName);
        System.out.println("beifen  是" + path);
        System.out.println("pathMysqldump  是" + pathMysqldump);
        // 判断文件夹是否存在，不存在则创建
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }
        // 调用备份方法
        System.out.println("备份开始！");
        BeifenUtil.beifenMySQL(path, pathMysqldump, s1);
        System.out.println("备份成功！");
        // 备份完成后发送邮件，进行网络备份
        System.out.println("开始发送邮件，进行网络备份！");
        BeifenUtil.sendMail(smf.format(d), s1, path);
        System.out.println("网络备份成功！");
        ModelAndView mv = new ModelAndView();
        mv.addObject("path", request.getSession().getServletContext().getRealPath("beifen"));
        mv.addObject("path1", s1);
        mv.setViewName("jsp/beifenSuccess.jsp");
        return mv;
    }

    // 用户退出功能
    @RequestMapping("out")
    public String out(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 从session中取出登录对象
        /*Object user = request.getSession().getAttribute("user");  放在了session销毁监听得代码中移除user
        if (user != null) {
            request.getSession().removeAttribute("user");
        }*/
        //session 执行 invalidate 方法  也能监听到session失效 从而 去改变redis中用户状态
        request.getSession().invalidate();
        return "redirect:login";
    }

    //系统用户的权限检查(前台校验)
    @ResponseBody
    @RequestMapping("checkUserPermission")
    public String checkUserPermission(HttpServletRequest request,String url){
        List<MenuPermission> permissionList = (List<MenuPermission>) request.getSession().getAttribute("permissionList");
        for (MenuPermission permission : permissionList){
            if(org.apache.commons.lang.StringUtils.isNotBlank(permission.getUrl())){
                if(url.equals(permission.getUrl())){
                    return "true";
                }
            }
        }
        return "false";
    }

    //个人信息查看
    @RequestMapping(value = "getUserInfoByUserId", produces = "text/html;charset=utf-8")
    public ModelAndView getUserInfoByUserId(HttpServletRequest request) throws MyException {
        User user = (User) request.getSession().getAttribute("user");
        ModelAndView mv = new ModelAndView();
        mv.addObject("userInfo", userService.getUserById(user.getId()));
        mv.setViewName("jsp/operatUserInfo.jsp");
        return mv;
    }

    //个人信息修改
    @ResponseBody
    @RequestMapping(value = "updateUserInfoByUserId" , method = RequestMethod.POST , produces = "text/html;charset=utf-8")
    public String updateUserInfoByUserId(HttpServletRequest request,User user){
        Map<String,Object> results = new HashMap<>();
        if(StringUtils.isEmpty(user.getImg()) && StringUtils.isEmpty(user.getNickName()) && StringUtils.isEmpty(user.getPassword())){
            results.put("msg","个人信息保存失败！");
            results.put("result",false);
            return JSON.toJSONString(results);
        }
        User loginUser = (User) request.getSession().getAttribute("user");
        try{
            user.setId(loginUser.getId());
            String fileName = UUID.randomUUID().toString(); // 保存在服务器的图片名称
            String grayFileName = "gray-"+ fileName; //保存在服务器的图片名称  注：经过置灰处理之后的图片名称
            if(StringUtils.isNotEmpty(user.getImg())){
                String temp = user.getImg().split(";")[0].split("/")[1];
                if("jpeg".equals(temp) || "gif".equals(temp) || "png".equals(temp) || "bmp".equals(temp)){
                    fileName += "." + temp;
                    grayFileName += "." + temp;
                }else{
                    // 非图片
                    results.put("msg","图片格式错误！个人信息保存失败！");
                    results.put("result",false);
                    return JSON.toJSONString(results);
                }
                // 前台Base64 转化为 图片
                Base64AndPictureUtil.Base64ToImage(user.getImg().split(",")[1],Constants.filePath + System.getProperty("file.separator") + fileName);
                // 图片置灰 并输出图片
                File temp_file = new File(Constants.filePath + System.getProperty("file.separator") + fileName);
                BufferedImage bufferedImage = ImageIO.read(temp_file);
                BufferedImage grayImage = Base64AndPictureUtil.getGrayImage(bufferedImage);
                //输出目录+输出文件
                File out = new File(Constants.filePath + System.getProperty("file.separator") + grayFileName);
                ImageIO.write(grayImage, "png",out);
                //上传图片到ftp服务器
                List<String> fileNames = new ArrayList<>();
                fileNames.add(fileName);
                fileNames.add(grayFileName);
                //上传
                FtpUtil.uploadLocalFile("/",fileNames);
                //删除临时图片
                temp_file.delete();
                out.delete();
                //删除ftp服务器上用户之前的头像
                fileNames.clear();
                if(!"default_picture.png".equals(loginUser.getImg())){
                    fileNames.add(loginUser.getImg());
                }
                if(!"default_picture.png".equals(loginUser.getGrayImg())){
                    fileNames.add(loginUser.getGrayImg());
                }
                if(fileNames.size() != 0){
                    FtpUtil.deleteFile("/",fileNames);
                }
                //新的图片信息再次存入session
                loginUser.setImg(fileName);
                loginUser.setGrayImg(grayFileName);

                user.setImg(fileName);
                user.setGrayImg(grayFileName);
            }
            //修改昵称
            if(StringUtils.isNotEmpty(user.getNickName())){
                loginUser.setNickName(user.getNickName());
            }
            //修改密码
            if(StringUtils.isNotEmpty(user.getPassword())){
                String newPassword = EncryptionAndDecryptionUtil.getEncryptionPassword(user.getPassword());
                loginUser.setPassword(newPassword);
                user.setPassword(newPassword);
            }
            request.getSession().setAttribute("user",loginUser);
            userService.updateUserInfoByUserId(user);

            //将用户信息修改到redis中
            if(Constants.webSocketChatSwitch){
                Jedis jedis = null;
                try {
                    jedis = RedisDB.getJedis();
                    jedis.select(RedisDB.dbSelectedForSystem);
                    updateUserInfoToRedis(user,fileName,grayFileName,jedis);
                }catch (Exception e){
                    System.out.println("修改个人用户到redis时失败！");
                    e.printStackTrace();
                    results.put("msg","服务器内部错误！");
                    results.put("result",false);
                    return JSON.toJSONString(results);
                }finally {
                    RedisDB.returnResource(jedis);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            results.put("msg","服务器内部错误！");
            results.put("result",false);
            return JSON.toJSONString(results);
        }
        results.put("msg","个人信息保存成功！");
        results.put("result",true);
        results.put("img",Constants.staticPictureHost + loginUser.getImg());
        results.put("nickName",loginUser.getNickName());
        return JSON.toJSONString(results);
    }

    /**
     *  将用户信息修改到redis中
     * @param user  要修改的用户
     * @param fileName  在线头像
     * @param grayFileName  离线头像
     */
    public void updateUserInfoToRedis(User user,String fileName,String grayFileName,Jedis jedis) {
        List<Map<String,Object>> allStrutsUsers = SerializeUtil.unserializeForList(jedis.get(RedisDB.systemUsers.getBytes()));

        //新的图片信息再次存入redis的联系人列表中
        if(StringUtils.isNotEmpty(user.getImg())) {
            for (Map temp_u : allStrutsUsers) {
                if (temp_u.get("id").equals(user.getId())) {
                    temp_u.put("img", Constants.staticPictureHost + fileName);
                    temp_u.put("grayImg", Constants.staticPictureHost + grayFileName);
                }
            }
        }
        //修改昵称
        if(StringUtils.isNotEmpty(user.getNickName())){
            for (Map temp_u : allStrutsUsers) {
                if(temp_u.get("id").equals(user.getId())){
                    temp_u.put("nickName",user.getNickName());
                }
            }
        }
        // 有头像修改 或者 昵称修改时  修改信息也要同步到redis
        if(StringUtils.isNotEmpty(user.getImg()) || StringUtils.isNotEmpty(user.getNickName())){
            jedis.set(RedisDB.systemUsers.getBytes(), SerializeUtil.serialize(allStrutsUsers));
        }
    }

    /**
     *  角色界面 开始
     */
    //系统角色管理
    @RequestMapping("getAllRoles")
    public ModelAndView getAllRoles(){
        //所有角色列表
        List<Role> roleList = userService.getAllRoles();
        ModelAndView mv = new ModelAndView();
        Gson gson = new Gson();
        mv.addObject("roles", roleList);
        mv.setViewName("jsp/systemRoleManage.jsp");
        return mv;
    }

    //添加系统角色
    @ResponseBody
    @RequestMapping(value = "createRole" , method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String createRole(Role role){
        try{
            role.setUpdate_time(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            role.setStatus(1);
            if(StringUtils.isBlank(role.getRemark()))
                role.setRemark("暂无备注");
            userService.createRole(role);
            return "角色创建成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "服务器内部错误！";
        }
    }

    //修改系统角色的信息
    @ResponseBody
    @RequestMapping(value = "updateRole" , method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String updateRole(Role role){
        try{
            role.setUpdate_time(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            if(StringUtils.isBlank(role.getRemark()))
                role.setRemark("暂无备注");
            userService.updateRole(role);
            return "角色信息修改成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "服务器内部错误！";
        }
    }

    //修改系统角色的状态
    @ResponseBody
    @RequestMapping(value = "updateRoleStatus" , method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String updateRoleStatus(Role role){
        try{
            role.setUpdate_time(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            userService.updateRoleStatus(role);
            return "角色状态修改成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "服务器内部错误！";
        }
    }

    /**
     *  用户与角色关联关系界面 开始
     */
    //用户与角色的列表查询
    @RequestMapping("getAllUsersAndAllRoles")
    public ModelAndView getAllUsersAndAllRoles(){
        ModelAndView mv = new ModelAndView();
        Gson gson = new Gson();
        //所有用户列表
        List<User> userList = userService.getAllUsers();
        System.out.println(userList);
        mv.addObject("users", userList);
        //所有角色列表
        List<Role> roleList = userService.getAllRoles();
        mv.addObject("roles", roleList);
        mv.setViewName("jsp/systemUserAndRoleMapping.jsp");
        return mv;
    }

    //根据用户id获取角色集合
    @ResponseBody
    @RequestMapping("getRolesByUserId")
    public List<Integer> getRolesByUserId(int userId){
        List<Integer> roles = userService.getRolesByUserId(userId);
        if(roles == null || roles.size() == 0)
            roles = new ArrayList<>();
        return roles;
    }

    //保存用户与角色的关联关系
    @ResponseBody
    @RequestMapping(value = "saveUserAndRolesMapping" , method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String saveUserAndRolesMapping(int userId,int []roleIds){
        try {
            userService.deleteUserAndRolesMappingsByUserId(userId);
            if(null != roleIds || roleIds.length !=0 ){
                userService.insertUserAndRolesMappings(userId,roleIds);
            }
            return "操作成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "服务器内部错误！";
        }
    }

    /**
     *  角色与菜单关联关系 开始
     */
    //菜单权限管理    查询出所有的菜单
    @RequestMapping("getRolesAndMenus")
    public ModelAndView getRolesAndMenus(HttpServletRequest request){
        //所有菜单列表，包括permission
        List<MenuPermission> menuListtemp = MenuPermissionUtil.getMenuTree(menuPermissionService.getAllMenu());
        //角色列表
        List<Role> roleList = userService.getAllRoles();
        Gson gson = new Gson();
        ModelAndView mv = new ModelAndView();
        mv.addObject("roles", roleList);
        mv.addObject("menus", gson.toJson(menuListtemp));
        mv.setViewName("jsp/systemRoleAndMenuMapping.jsp");
        return mv;
    }

    /**
     * 查询角色所拥有的权限并在页面上生成对应的tree
     * @param rid
     * @return
     */
    @ResponseBody
    @RequestMapping("getMenuPermissionByRoleId")
    public List<Integer> getMenuPermissionByRoleId(int rid){
        return menuPermissionService.getMenuPermissionByRoleId(rid);
    }

    /**
     * 修改用户拥有的权限
     * @param rid
     * @param permissionsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "insertMenuPermissionByRoleId", method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String insertUserMenuPermissionByUserId(int rid,int[] permissionsId){
        try{
            //先删除
            menuPermissionService.deleteMenuPermissionByRoleId(rid);
            if(permissionsId != null){
                //后添加
                menuPermissionService.insertMenuPermissionByRoleId(rid,permissionsId);
            }
            return "操作成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "操作失败！";
        }
    }

    /**
     *  用户相关开始
     */
    //系统用户管理
    @RequestMapping("getAllUsers")
    public ModelAndView getAllUsers(){
        //所有用户列表
        List<User> userList = userService.getAllUsers();
        ModelAndView mv = new ModelAndView();
        Gson gson = new Gson();
        mv.addObject("users", userList);
        mv.setViewName("jsp/systemUserManage.jsp");
        return mv;
    }

    //新建系统用户
    @ResponseBody
    @RequestMapping(value = "createManageUser" , method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String createManageUser(User user){
        try{
            user.setTime(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            user.setNickName(user.getName());
            user.setImg("default_picture.png");
            user.setGrayImg("default_picture.png");
            user.setIp("XX");
            user.setUserRegion("XX");
            user.setUserCity("XX");
            user.setStruts("1");
            user.setPassword(EncryptionAndDecryptionUtil.getEncryptionPassword(user.getPassword())); //获取加密后的密码;
            userService.createUser(user);
            return "用户创建成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "服务器内部错误！";
        }
    }

    //为系统用户重置密码
    @ResponseBody
    @RequestMapping(value = "resettingPasswordForUserByUserId" , method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String resettingPasswordForUserByUserId(int uid , String newPwd){
        try{
            String enctyptionPwd = EncryptionAndDecryptionUtil.getEncryptionPassword(newPwd); //获取加密后的密码
            userService.updateUserPwdByUid(uid,enctyptionPwd);  //重置系统用户密码
            return "密码更新成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "服务器内部错误！";
        }
    }

    //禁用、启用系统用户
    @ResponseBody
    @RequestMapping(value = "updateUserStrutsByUid" , method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String updateUserStrutsByUid(int uid ){
        try{
            User user = userService.getUserById(uid);
            if(user.getStruts().equals("1")){
                //修改系统用户状态
                userService.updateUserStrutsByUid(uid,0);
            }else{
                //修改系统用户状态
                userService.updateUserStrutsByUid(uid,1);
            }
            return "操作成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "操作失败！";
        }
    }

    //菜单权限配置
    @RequestMapping("menuTreeConfigure")
    public ModelAndView menuTreeConfigure(HttpServletRequest request) throws MyException {
        throw new MyException("本功能暂未开放！感谢使用！");
    }
}