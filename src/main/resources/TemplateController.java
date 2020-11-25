package packagename;
import appPath.utils.Response;
import beanname;
import servicename;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("jin-generator-controller-firstname/Temp-nam")
public class TemplateController {
    @Autowired
    private TemplateService templateService;
    @PostMapping("templateList")
    public Response templateList(Integer page, Integer size, Template template){
        return templateService.templateList(page,size,template);
    }
    @PostMapping("templateFindById")
    public Response templateFindById(Integer templateId){
        return templateService.templateFindById(templateId);
    }
    @PostMapping("addTemplate")
    public Response addTemplate(Template template){
        return templateService.addTemplate(template);
    }
    @PostMapping("upTemplate")
    public Response upTemplate(Template template){
        return templateService.upTemplate(template);
    }
    @PostMapping("delTemplate")
    public Response delTemplate(Integer templateId){
        return templateService.delTemplate(templateId);
    }
}