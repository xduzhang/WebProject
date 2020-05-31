package xx.community.community.cache;

import xx.community.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO block1=new TagDTO();
        block1.setCategoryName("分区1");
        block1.setTags(Arrays.asList("1","2","3"));
        tagDTOS.add(block1);
        TagDTO block2=new TagDTO();
        block1.setCategoryName("分区2");
        block1.setTags(Arrays.asList("1","2","3"));
        tagDTOS.add(block2);
        return  tagDTOS;
    }
}
