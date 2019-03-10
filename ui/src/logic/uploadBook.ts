function uploadBook(book: File): Promise<Response> {
    const formData = new FormData();
    formData.append("file", book, book.name);
    return fetch("/api/v1/books/upload", {
        body: formData,
        method: "POST",
    });
}

export default uploadBook;
