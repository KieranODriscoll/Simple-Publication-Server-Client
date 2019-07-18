import java.io.Serializable;

public class Book implements Serializable {

	String ISBN;
	String title;
	String author;
	String publisher;
	String publicationYear;

	public Book(String ISBN, String title, String author, String publisher, String publicationYear) {
		this.ISBN = ISBN;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.publicationYear = publicationYear;
	}

	@Override
	public boolean equals(Object x) {

		if (x instanceof Book) {
			if (this.ISBN != null) {
				return this.ISBN.equals(((Book) x).ISBN);
			} else {
				if (title != null && author == null && publisher == null && publicationYear == null) {
					return this.title.equals(((Book) x).title);
				} else if (title == null && author != null && publisher == null && publicationYear == null) {
					return this.author.equals(((Book) x).author);
				} else if (title == null && author == null && publisher != null && publicationYear == null) {
					return this.publisher.equals(((Book) x).publisher);
				} else if (title == null && author == null && publisher == null && publicationYear != null) {
					return this.publicationYear.equals(((Book) x).publicationYear);
				} else if (title != null && author != null && publisher == null && publicationYear == null) {
					return this.title.equals(((Book) x).title) && this.author.equals(((Book) x).author);
				} else if (title != null && author == null && publisher != null && publicationYear == null) {
					return this.title.equals(((Book) x).title) && this.publisher.equals(((Book) x).publisher);
				} else if (title != null && author == null && publisher == null && publicationYear != null) {
					return this.title.equals(((Book) x).title)
							&& this.publicationYear.equals(((Book) x).publicationYear);
				} else if (title == null && author != null && publisher == null && publicationYear != null) {
					return this.author.equals(((Book) x).author)
							&& this.publicationYear.equals(((Book) x).publicationYear);
				} else if (title == null && author == null && publisher != null && publicationYear != null) {
					return this.publisher.equals(((Book) x).publisher)
							&& this.publicationYear.equals(((Book) x).publicationYear);
				} else if (title == null && author != null && publisher != null && publicationYear == null) {
					return this.author.equals(((Book) x).author) && this.publisher.equals(((Book) x).publisher);
				} else if (title != null && author != null && publisher != null && publicationYear == null) {
					return this.title.equals(((Book) x).title) && this.author.equals(((Book) x).author)
							&& this.publisher.equals(((Book) x).publisher);
				} else if (title != null && author != null && publisher == null && publicationYear != null) {
					return this.title.equals(((Book) x).title) && this.author.equals(((Book) x).author)
							&& this.publicationYear.equals(((Book) x).publicationYear);
				} else if (title != null && author == null && publisher != null && publicationYear != null) {
					return this.title.equals(this.title) && this.publisher.equals(this.publisher)
							&& this.publicationYear.equals(this.publicationYear);
				} else if (title == null && author != null && publisher != null && publicationYear != null) {
					return this.author.equals(((Book) x).author) && this.publisher.equals(((Book) x).publisher)
							&& this.publicationYear.equals(((Book) x).publicationYear);
				}else if (title != null && author != null && publisher != null && publicationYear != null) {
					return  this.title.equals(((Book) x).title) && this.author.equals(((Book) x).author) && this.publisher.equals(((Book) x).publisher)
							&& this.publicationYear.equals(((Book) x).publicationYear);
				}

			}
		}

		return false;

	}

}
