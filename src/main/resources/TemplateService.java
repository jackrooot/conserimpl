package packagename;

import appPath.utils.Response;
import beanname;
public interface TemplateService {
    Response templateList(Integer page, Integer size, Template template);
    Response addTemplate(Template template);
    Response templateFindById(Integer templateId);
    Response upTemplate(Template template);
    Response delTemplate(Integer templateId);
}
