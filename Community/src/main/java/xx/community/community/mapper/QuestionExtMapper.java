package xx.community.community.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import xx.community.community.model.Question;
import xx.community.community.model.QuestionExample;

import java.util.List;

public interface QuestionExtMapper {
  List<Question> selectRelated(Question question);
}