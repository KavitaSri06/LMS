package com.library.librarymanagement.controller;

import com.library.librarymanagement.model.Book;
import com.library.librarymanagement.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books/index";
    }

    @GetMapping("/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/add";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "books/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        Book existing = bookService.getBookById(id);

        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setCategory(book.getCategory());
        existing.setQuantity(book.getQuantity());

        bookService.saveBook(existing);

        return "redirect:/books";
    }
    @GetMapping("/issue/{id}")
    public String issueBook(@PathVariable Long id) {
        Book book = bookService.getBookById(id);

        if (book.getQuantity() > 0) {
            book.setQuantity(book.getQuantity() - 1);
            bookService.saveBook(book);
        }

        return "redirect:/books";
    }

    @GetMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        Book book = bookService.getBookById(id);

        book.setQuantity(book.getQuantity() + 1);
        bookService.saveBook(book);

        return "redirect:/books";
    }
    @GetMapping("/search")
    public String searchBooks(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("books", bookService.searchBooks(keyword));
        return "books/index";
    }
}