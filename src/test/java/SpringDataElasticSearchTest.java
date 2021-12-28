import com.itheima.entity.Article;
import com.itheima.repositories.ArticleRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

/**
 * @author zjc
 * @create 2020--03--04--16:58
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringDataElasticSearchTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Test
    public void createIndex() throws Exception{
        //创建索引并配置映射
        template.createIndex(Article.class);
        //配置映射关系
        //template.putMapping(Article.class)
    }

    @Test
    public void AddDocument() throws Exception{
        for (int i = 10; i <= 20; i++) {
            //创建一个article对象
            Article article = new Article();
            article.setId(i);
            article.setTitle("java我学了半年但是我觉得我学了好久我要继续"+i);
            article.setContent("2019年我过的很充实,我想今年也是,想你也是");
            articleRepository.save(article);

        }
    }

    @Test
    public void DeleDocumentById() throws Exception{
        articleRepository.deleteById(2l);

        /*//修改   和lucene一样 只有添加  覆盖就是更新\
        //创建一个article对象
        Article article = new Article();
        article.setId(2);
        article.setTitle("C#是一门面向对象编程语言");
        article.setContent("简单易学,是目前最火热的编程语言,ta和我的伙伴都在学");
        articleRepository.save(article);*/
    }

    @Test
    public void findAll() throws Exception{
        Iterable<Article> articles = articleRepository.findAll();
        articles.forEach(a-> System.out.println(a));
    }

    @Test
    public void findById() throws Exception{
        Optional<Article> optional = articleRepository.findById(1l);
        Article article = optional.get();
        System.out.println(article);
    }

    @Test
    public void testFindByTitle() throws Exception{
        List<Article> list = articleRepository.findByTitle("编程语言面向对象");
        list.stream().forEach(a -> System.out.println(a));
    }

    @Test
    public void testFindByTitleOrContent() throws Exception{
        List<Article> articleList = articleRepository.findByTitleOrContent("编程语言","学习");
        articleList.stream().forEach(a -> System.out.println(a));
    }
    @Test
    public void testFindByTitleOrContent2() throws Exception{
        Pageable pageable = PageRequest.of(0,5);
        List<Article> articleList = articleRepository.findByTitleOrContent("编程语言","学习",pageable);
        articleList.stream().forEach(a -> System.out.println(a));
    }

    @Test
    public void testNativeSearchQuery() throws Exception {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery("我是一个好孩子").defaultField("title"))
                .withPageable(PageRequest.of(0,5))
                .build();

        //执行查询
        List<Article> articleList = template.queryForList(query, Article.class);
        articleList.forEach(a-> System.out.println(a));
    }


}



