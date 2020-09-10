package com.zh.blog.web.admin;

import com.zh.blog.po.Tag;
import com.zh.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    @GetMapping("/tags/{id}/input")
    public String  editInput(@PathVariable("id") Long id, Model model) {
        Tag tag = tagService.getTag(id);
        model.addAttribute("tag", tag);
        return "admin/tags-input";
    }

    @PostMapping("/tags")
    public String post(Tag tag, RedirectAttributes attributes) {
        if (tag.getName() == null || tag.getName().equals("")) {
            attributes.addFlashAttribute("message", "标签不能为空");
            return "redirect:/admin/tags/input";
        }
        Tag t1 = tagService.getTagByName(tag.getName());
        if (t1 != null) {
            attributes.addFlashAttribute("message", "不能添加重复的标签");
            return "redirect:/admin/tags/input";
        }
        Tag t = tagService.saveTag(tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String post(Tag tag, @PathVariable("id") long id, RedirectAttributes attributes) {
        if (tag.getName() == null || tag.getName().equals("")) {
            attributes.addFlashAttribute("message", "标签不能为空");
            return "redirect:/admin/tags/input";
        }
        Tag t1 = tagService.getTagByName(tag.getName());
        if (t1 != null) {
            attributes.addFlashAttribute("message", "不能添加重复的标签");
            return "redirect:/admin/tags/input";
        }
        Tag t = tagService.updateTag(id, tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "修改失败");
        } else {
            attributes.addFlashAttribute("message", "修改成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }

}
