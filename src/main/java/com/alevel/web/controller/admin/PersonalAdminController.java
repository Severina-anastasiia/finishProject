package com.alevel.web.controller.admin;

import com.alevel.facade.PersonalAdminFacade;
import com.alevel.web.controller.AbstractController;
import com.alevel.web.data.request.PersonalData;
import com.alevel.web.data.response.PageData;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alevel.util.WebRequestUtil.DEFAULT_ORDER_PARAM_VALUE;

@Controller
@RequestMapping("/admin/personals")
public class PersonalAdminController extends AbstractController {

    private final PersonalAdminFacade personalAdminFacade;

    public PersonalAdminController(PersonalAdminFacade personalAdminFacade) {
        this.personalAdminFacade = personalAdminFacade;
    }

    @GetMapping("/all")
    public String findAll(WebRequest request, Model model) {
        PageData<PersonalData> personalPageData = personalAdminFacade.findAll(request);
        String[] columnNames = new String[] { "#", "id", "created", "email", "enabled", "details"};
        List<HeaderData> headerDataList = new ArrayList<>();
        for (String column : columnNames) {
            HeaderData data = new HeaderData();
            data.setHeaderName(column);
            if (column.equals("#") || column.equals("details")) {
                data.setSortable(false);
            } else {
                data.setSortable(true);
                data.setSort(column);
                if (personalPageData.getSort().equals(column)) {
                    data.setActive(true);
                    data.setOrder(personalPageData.getOrder());
                } else {
                    data.setActive(false);
                    data.setOrder(DEFAULT_ORDER_PARAM_VALUE);
                }
            }
            headerDataList.add(data);
        }
        model.addAttribute("pageData", personalPageData);
        model.addAttribute("headerDataList", headerDataList);
        return "page/admin/personal_all";
    }

    @PostMapping("/all")
    public ModelAndView findAllRedirect(WebRequest request, ModelMap model) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (MapUtils.isNotEmpty(parameterMap)) {
            parameterMap.forEach((key, value) -> {
                if (!key.equals("_csrf")) {
                    model.addAttribute(key, value);
                }
            });
        }
        return new ModelAndView("redirect:/admin/personals/all", model);
    }

    @GetMapping("/details/{id}")
    public String findById(Model model, @PathVariable Integer id) {
        model.addAttribute("personal", personalAdminFacade.findById(id));
        return "page/admin/personal_details";
    }

    @GetMapping("/details/{id}/lock")
    public String lock(@PathVariable Integer id) {
        personalAdminFacade.lockAccount(id);
        return "redirect:/admin/personals/details/" + id;
    }

    @GetMapping("/details/{id}/unlock")
    public String unlock(@PathVariable Integer id) {
        personalAdminFacade.unlockAccount(id);
        return "redirect:/admin/personals/details/" + id;
    }

    @Getter
    @Setter
    public static class HeaderData {

        private String headerName;
        private boolean active;
        private boolean sortable;
        private String sort;
        private String order;
    }
}
