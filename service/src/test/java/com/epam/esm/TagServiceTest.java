//package com.epam.esm;
//
//
//import com.epam.esm.converter.TagConverter;
//import com.epam.esm.exception.EntityNotFoundException;
//import com.epam.esm.impl.TagServiceImpl;
//import com.epam.esm.model.Tag;
//import com.epam.esm.model.TagDTO;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class TagServiceTest {
//
//    @Mock
//    private TagRepository tagRepository;
//    @Mock
//    private TagConverter tagConverter;
//    @Mock
//    private RelationRepository relationRepository;
//
//    private TagService tagService;
//
//    @Before
//    public void init() {
//        tagService = new TagServiceImpl(tagRepository, tagConverter, relationRepository);
//    }
//
//    @Test
//    public void shouldGetTagDTOListForFindAllMethodCall() {
//        Tag tag = new Tag();
//        tag.setName("Tag1");
//        TagDTO tagDTO = new TagDTO();
//        List<Tag> tags = Collections.singletonList(tag);
//        when(tagRepository.findAll()).thenReturn(tags);
//        when(tagConverter.toDTO(tag)).thenReturn(tagDTO);
//        List<TagDTO> returnedTags = tagService.findAll();
//        assertEquals(tagDTO, returnedTags.get(0));
//    }
//
//    @Test
//    public void shouldExecuteMethodCreateTagWithoutExceptions() {
//        Tag tag = new Tag();
//        tag.setId(1L);
//        tag.setName("Tag1");
//        TagDTO tagDTO = new TagDTO();
//        tagDTO.setId(1L);
//        tagDTO.setName("Tag1");
//        when(tagConverter.toEntity(tagDTO)).thenReturn(tag);
//        tagService.create(tagDTO);
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void shouldThrowExceptionWhenCatchingExceptionFromTagRepositoryForCreateMethodCall() {
//        Tag tag = new Tag();
//        tag.setId(1L);
//        tag.setName("Tag1");
//        TagDTO tagDTO = new TagDTO();
//        tagDTO.setId(1L);
//        tagDTO.setName("Tag1");
//        when(tagRepository.create(any())).thenThrow(new RuntimeException());
//        tagService.create(tagDTO);
//    }
//
//    @Test
//    public void shouldGetTagDTOForFindByIdMethodCall() {
//        Tag tag = new Tag();
//        tag.setId(1L);
//        tag.setName("Tag1");
//
//        TagDTO tagDTO = new TagDTO();
//        tagDTO.setId(1L);
//        tagDTO.setName("Tag1");
//
//        when(tagRepository.findById(tag.getId())).thenReturn(tag);
//        when(tagConverter.toDTO(tag)).thenReturn(tagDTO);
//        TagDTO tagById = tagService.findById(tagDTO.getId());
//        assertEquals(tagDTO, tagById);
//    }
//
//    @Test(expected = EntityNotFoundException.class)
//    public void shouldThrowDaoExceptionWhenCatchingExceptionFromRepositoryForFindByIdMethodCall() {
//        Long id = 1L;
//        when(tagRepository.findById(id)).thenThrow(new EntityNotFoundException("Tag not found with id"));
//        tagService.findById(id);
//    }
//
//    @Test(expected = EntityNotFoundException.class)
//    public void shouldThrowServiceExceptionForDeleteMethodCall() {
//        Long id = 1L;
//        Optional<Tag> tag = Optional.of(new Tag());
//        tag.get().setId(id);
//        tag.get().setName("Tag1");
//        when(tagRepository.findById(id)).thenThrow(new EntityNotFoundException("Tag not found with id " + id));
//        tagService.delete(id);
//    }
//
//    @Test
//    public void shouldTrueForDeleteMethodCall() {
//        Tag tag =new Tag();
//        tag.setId(1L);
//        tag.setName("Tag1");
//        when(tagRepository.findById(tag.getId())).thenReturn(tag);
//        when(tagRepository.delete(tag.getId())).thenReturn(true);
//        assertEquals(true, tagService.delete(tag.getId()));
//    }
//}