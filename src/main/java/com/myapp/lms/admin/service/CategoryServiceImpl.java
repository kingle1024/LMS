package com.myapp.lms.admin.service;

import com.myapp.lms.admin.dto.CategoryDto;
import com.myapp.lms.admin.entity.Category;
import com.myapp.lms.admin.mapper.CategoryMapper;
import com.myapp.lms.admin.model.CategoryInput;
import com.myapp.lms.admin.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private Sort getSortBySortValueDesc(){
        return Sort.by(Sort.Direction.DESC, "sortValue");
    }
    @Override
    public List<CategoryDto> list() {
        List<Category> categories = categoryRepository.findAll(getSortBySortValueDesc());
        return CategoryDto.of(categories);
    }

    @Override
    public boolean add(String categoryName) {
        Optional<Category> optionalCategory
                = categoryRepository.findByCategoryName(categoryName);
        if(optionalCategory.isPresent()){
            return false;
        }
        Category category = Category.builder()
                .categoryName(categoryName)
                .usingYn(true)
                .sortValue(0)
                .build();
        categoryRepository.save(category);
        return true;
    }

    @Override
    public boolean update(CategoryInput parameter) {
        Optional<Category> optionalCategory =
                categoryRepository.findById(parameter.getId());
        if(!optionalCategory.isPresent()){
            return false;
        }
        Category category = Category.builder()
                .id(parameter.getId())
                .categoryName(parameter.getCategoryName())
                .usingYn(parameter.isUsingYn())
                .sortValue(parameter.getSortValue())
                .build();
        categoryRepository.save(category);
        return true;
    }

    @Override
    public boolean del(long id) {
        categoryRepository.deleteById(id);
        return true;
    }

    @Override
    public List<CategoryDto> frontList(CategoryDto parameter) {
        return categoryMapper.select(parameter);
    }
}
