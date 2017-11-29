package com.pluralsight.bookstore.repository;

import com.pluralsight.bookstore.model.Book;
import com.pluralsight.bookstore.model.Language;
import com.pluralsight.bookstore.util.IsbnGenerator;
import com.pluralsight.bookstore.util.NumberGenerator;
import com.pluralsight.bookstore.util.TextUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class BookRepositoryTest {

    @Inject
    private BookRepository bookRepository;
    @Test
    public void create() throws Exception {
        assertEquals(Long.valueOf(0),bookRepository.countAll());
        assertEquals(0, bookRepository.findAll().size());

        //create a book
        Book book = new Book("isin", "a  title", 12F, 123, Language.ENGLISH, new Date(), "blag blag");
        book = bookRepository.create(book);
        Long bookId = book.getId();

        assertNotNull(bookId);

        Book bookFound = bookRepository.find(bookId);
        assertEquals("a title", bookFound.getTitle());
        assertTrue(bookFound.getIsbn().startsWith("13"));

        assertEquals(Long.valueOf(1),bookRepository.countAll());
        assertEquals(1, bookRepository.findAll().size());

         bookRepository.delete(bookId);

        assertEquals(Long.valueOf(0),bookRepository.countAll());
        assertEquals(0, bookRepository.findAll().size());
    }

    @Test(expected = Exception.class)
    public void createInvalidBook(){
        Book book = new Book("isin", null, 12F, 123, Language.ENGLISH, new Date(), "blag blag");
        book = bookRepository.create(book);
    }

    @Test(expected = Exception.class)
    public void FindBookWithInvalidID(){
        bookRepository.find(null);
    }


    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(BookRepository.class)
                .addClass(Book.class)
                .addClass(Language.class)
                .addClass(TextUtil.class)
                .addClass(NumberGenerator.class)
                .addClass(IsbnGenerator.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml");
    }

}
