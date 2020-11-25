package packagename;

import beanname;
import mappername;
import servicename;
import appPath.utils.Response;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class TemplateServiceImpl implements TemplateService{
    @Autowired
    private TemplateMapper templateMapper;
    @Override
    public Response templateList(Integer page, Integer size, Template template) {
        if(page==null||size==null){
            page=1;size=10;
        }
        PageHelper.startPage(page,size);
        List<Template> templates = templateMapper.selectByExample(null);
        return Response.res("0",new PageInfo<>(templates));
    }
    @Override
    public Response templateFindById(Integer templateId){
        Template template=templateMapper.selectByPrimaryKey(templateId);
        return Response.res("0",template);
    }
    @Override
    public Response addTemplate(Template template) {
        templateMapper.insertSelective(template);
        return Response.res("0","添加成功");
    }

    @Override
    public Response upTemplate(Template template) {
        templateMapper.updateByPrimaryKeySelective(template);
        return Response.res("0","修改成功");
    }

    @Override
    public Response delTemplate(Integer templateId) {
        templateMapper.deleteByPrimaryKey(templateId);
        return Response.res("0","删除成功");
    }
}
