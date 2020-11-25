package com.conserimpl;

import org.springframework.util.ClassUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:Jack.Jin
 * @Date:create in 9:12 2020/11/25
 */
public class Generator {
    public static HashMap<String, String> currentPath = getCurrentPath();
    public static void main(String[] args) {
        loadResource();
        ArrayList mappersName = getMappersName();
        generator(mappersName);
    }
    public static void generator(ArrayList mappersName){
        String appPath = currentPath.get("appPath");
        String appPathToResources = currentPath.get("appPathToResources");
        String controllerAllPath=appPath+"\\controller";
        String serviceAllPath=appPath+"\\"+currentPath.get("servicename");
        String serviceImplAllPath=serviceAllPath+"\\"+currentPath.get("serviceImplname");
        String templateController=appPathToResources+"TemplateController.java";
        String TemplateService=appPathToResources+"TemplateService.java";
        String TemplateServiceImpl=appPathToResources+"TemplateServiceImpl.java";
        File controlleFile = new File(controllerAllPath);
        File serviceFile = new File(serviceAllPath);
        File serviceImplFile = new File(serviceImplAllPath);
        if(!controlleFile.exists()&&!controlleFile.isDirectory()){
            controlleFile.mkdirs();
        }
        if(!serviceFile.exists()&&!serviceFile.isDirectory()){
            serviceFile.mkdirs();
        }
        if(!serviceImplFile.exists()&&!serviceImplFile.isDirectory()){
            serviceImplFile.mkdirs();
        }
        String controllerFileContent = getFileContent(templateController);
        String serviceFileContent = getFileContent(TemplateService);
        String serviceImplFileContent = getFileContent(TemplateServiceImpl);
        for (Object name : mappersName) {
            String controllerName=(String)name+"Controller";
            String serviceName=(String)name+"Service";
            String serviceImplName=(String)name+"ServiceImpl";
            String controllerFilePath=controllerAllPath+"\\"+controllerName+".java";
            String serviceFilePath=serviceAllPath+"\\"+serviceName+".java";
            String serviceImplFilePath=serviceImplAllPath+"\\"+serviceImplName+".java";
            System.out.println(controllerFilePath);
            System.out.println(serviceFilePath);
            System.out.println(serviceImplFilePath);
            String controllePath = currentPath.get("classRelativePathThrowClassName")+".controller";
            String servicePath = currentPath.get("classRelativePathThrowClassName")+"."+currentPath.get("servicename");
            String serviceImplPath = servicePath+"."+currentPath.get("serviceImplname");
            String beanname=currentPath.get("classRelativePathThrowClassName")+"."+currentPath.get("beanname")+"."+name;
            String serviceFileConts = serviceFileContent
                    .replaceAll("Template", (String) name)
                    .replaceAll("template", ((String) name).toLowerCase())
                    .replace("appPath",currentPath.get("classRelativePathThrowClassName"))
                    .replace("packagename", servicePath)
                    .replace("beanname", beanname);
            String serviceImplFileConts = serviceImplFileContent
                    .replaceAll("Template", (String) name)
                    .replaceAll("template", ((String) name).toLowerCase())
                    .replace("appPath",currentPath.get("classRelativePathThrowClassName"))
                    .replace("servicename",currentPath.get("classRelativePathThrowClassName")+"."+currentPath.get("servicename")+"."+((String) name)+"Service")
                    .replace("packagename", serviceImplPath)
                    .replace("mappername",currentPath.get("classRelativePathThrowClassName")+"."+currentPath.get("mappername")+"."+name+"Mapper")
                    .replace("beanname", beanname);
            String controllerFileConts = controllerFileContent
                    .replaceAll("Template", (String) name)
                    .replaceAll("template", ((String) name).toLowerCase())
                    .replace("appPath",currentPath.get("classRelativePathThrowClassName"))
                    .replace("packagename",controllePath)
                    .replace("beanname",beanname)
                    .replace("servicename",currentPath.get("classRelativePathThrowClassName")+"."+currentPath.get("servicename")+"."+((String) name)+"Service")
                    .replace("jin-generator-controller-firstname",currentPath.get("jin-generator-controller-firstname"))
                    .replace("Temp-nam",((String) name).toLowerCase());
            myFileWriter(controllerFilePath,controllerFileConts);
            myFileWriter(serviceFilePath,serviceFileConts);
            myFileWriter(serviceImplFilePath,serviceImplFileConts);
        }
    }
    public static void generatorService(ArrayList mappersName){
        String appPath = currentPath.get("appPath");
        String appPathToResources = currentPath.get("appPathToResources");
        String servicePath=appPath+"\\service";
        String templateService=appPathToResources+"TempateService.java";
        File file = new File(servicePath);
        if(!file.exists()&&!file.isDirectory()){
            file.mkdirs();
        }
        String fileContent = getFileContent(templateService);
        for (Object name : mappersName) {
            String mapperName=(String)name+"Controller";
            String controllerFilePath=servicePath+"\\"+mapperName+".java";
            System.out.println(controllerFilePath);;
            String fileCont = fileContent.replaceAll("Template", (String) name);
            String path = currentPath.get("classRelativePathThrowClassName")+".service";
            String beanname=currentPath.get("classRelativePathThrowClassName")+"."+currentPath.get("beanname")+"."+name;
            String fileConts = fileCont.replaceAll("template", (String) ((String) name)
                    .toLowerCase())
                    .replace("packagename",path)
                    .replace("beanname",beanname);
            myFileWriter(controllerFilePath,fileConts);
        }
    }

    public static ArrayList getMappersName(){
        ArrayList mappersName = new ArrayList();
        String appPath = currentPath.get("appPath");
        File file = new File(appPath + "\\"+currentPath.get("mappername"));
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for (File file1 : files) {
                String name = file1.getName();
                if(name.contains(".java")){
                    mappersName.add(name.substring(0,name.lastIndexOf("Mapper.")));
                }
            }
        }
        return mappersName;
    }
    public static void loadResource(){
        try {
            Yaml yaml = new Yaml();
            URL resource = ClassUtils.getDefaultClassLoader().getResource("application.yml");
            if(resource!=null){
                Map load = (Map)yaml.load(new FileInputStream(resource.getFile()));
                currentPath.put("jin-generator-controller-firstname",(String)load.get("jin-generator-controller-firstname"));
                currentPath.put("beanname",(String)load.get("beanname"));
                currentPath.put("servicename",(String) load.get("servicename"));
                currentPath.put("serviceImplname", (String) load.get("serviceImplname"));
                currentPath.put("mappername", (String) load.get("mappername"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getFileContent(String path){
        StringBuilder content = new StringBuilder();
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(path));
//            String tempStr;
//            while(!(tempStr=reader.readLine()).equals("esc")){
//                System.out.println(reader.readLine());
//                content.append(reader.readLine()+"\n");
//            }
//            reader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        int c=0;
        try {
            FileReader reader=new FileReader(path);
            c=reader.read();
            while(c!=-1) {
                content.append((char) c);
                c=reader.read();
            }
            reader.close();
        } catch (Exception e) {
              e.printStackTrace();
            }
        return content.toString();
    }
    public static int myFileWriter(String path,String content){
        try {
            if(new File(path).exists()){
                return 0;
            }
            FileWriter fileWriter = new FileWriter(path,true);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }
    public static HashMap<String, String> getCurrentPath(){
        String classRelativePath = Thread.currentThread().getStackTrace()[1].getClassName();
        String classRelativeThrowClassNamePath = classRelativePath.substring(0, classRelativePath.lastIndexOf("."));
        String className =classRelativePath.substring(classRelativePath.lastIndexOf(".")+1,classRelativePath.length());
        String systemWorkPath = System.getProperty("user.dir");
        String systemRelativePath = classRelativeThrowClassNamePath.replace(".", "\\");
        String path=systemWorkPath+"\\src\\main\\java\\"+systemRelativePath+"\\"+className+".java";
        String appPath=systemWorkPath+"\\src\\main\\java\\"+systemRelativePath;
        String appPathToResources=systemWorkPath+"\\src\\main\\resources\\";
        HashMap<String, String> stringStringHashMap = new HashMap<String, String>();
        stringStringHashMap.put("classRelativePath",classRelativePath);
        stringStringHashMap.put("classRelativePathThrowClassName",classRelativeThrowClassNamePath);
        stringStringHashMap.put("className",className);
        stringStringHashMap.put("systemWorkPath",systemWorkPath);
        stringStringHashMap.put("systemRelativePath",systemRelativePath);
        stringStringHashMap.put("path",path);
        stringStringHashMap.put("appPath",appPath);
        stringStringHashMap.put("appPathToResources",appPathToResources);
        return stringStringHashMap;
    }
}
