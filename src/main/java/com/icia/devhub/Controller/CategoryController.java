package com.icia.devhub.Controller;

import com.icia.devhub.Service.CategoryService;
import com.icia.devhub.dto.Order.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService csvc;

    @PostMapping("/category")
    @ResponseBody
    public List<CategoryDTO> CategoryList(@RequestParam("category") int category) {
        System.out.println("Received categoryName: " + category);
        return csvc.CategoryList(category);
    }
}
