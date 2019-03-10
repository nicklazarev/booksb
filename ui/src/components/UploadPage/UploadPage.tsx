import { Button, Card, CardActionArea, CardContent, CardMedia, Grid, LinearProgress, Toolbar } from "@material-ui/core";
import { createStyles, Theme, WithStyles, withStyles } from "@material-ui/core/styles";
import { WarningOutlined as WarningIcon } from "@material-ui/icons";
import classnames from "classnames";
import { debounce } from "debounce";
import * as React from "react";
import parseEpubFile from "../../logic/parseEpubFile";
import uploadBook from "../../logic/uploadBook";
import Dashboard from "../Dashboard";

const styles = (theme: Theme) => createStyles({
    cardToolbar: {
        background: theme.palette.background.default,
        bottom: theme.spacing.unit / 2,
        justifyContent: "flex-end",
        position: "absolute",
        width: "100%",
    },
    content: {
        alignItems: "center",
        background: theme.palette.background.default,
        bottom: 0,
        color: theme.palette.error.main,
        display: "flex",
        fontWeight: "bold",
        justifyContent: "center",
        position: "absolute",
        width: "100%",
    },
    grid: {
        padding: theme.spacing.unit * 3,
    },
    input: {
        display: "none",
    },
    media: {
        height: 280,
        maxHeight: 280,
    },
    preview: {
        height: 280,
        position: "relative",
        width: 200,
    },
    previewDisabled: {
        opacity: 0.5,
    },
    progress: {
        bottom: 0,
        position: "absolute",
        width: "100%",
    },
});

interface UploadPageProps extends WithStyles<typeof styles> {

}

interface Book {
    id: string | null;
    isbn: string | null;
    uploading: boolean | null;
    cover: string | null;
    error: string | null;
}

interface UploadPageState {
    books: Book[];
}

class UploadPage extends React.Component<UploadPageProps, UploadPageState> {

    public handleFilterChange = debounce((filter: string | null) => {
        this.fetchBooks(filter);
    }, 300);
    constructor(props: UploadPageProps) {
        super(props);
        this.state = { books: [] };
    }

    public fetchBooks = async (filter?: string | null) => {
        const response = await fetch(`/api/v1/books?name=${filter || ""}`);
        const books = await response.json();
        this.setState({ books });
    }

    public componentDidMount() {
        this.fetchBooks();
    }

    public handleFileUpload = async (event: React.FormEvent<HTMLInputElement>) => {
        event.preventDefault();
        const { files } = event.currentTarget;
        console.log(files);
        if (files != null) {
            const booksToUpload = [];
            for (let index = 0; index < files.length; index++) {
                const file = files.item(index);
                if (file == null) {
                    continue;
                }
                const book = await parseEpubFile(file);
                if (book.cover != null) {
                    const tbook: Book = { id: null, ...book, uploading: true, error: null };
                    booksToUpload.push(tbook);
                }
            }

            this.setState({ books: [...booksToUpload, ...this.state.books] });

            for (let index = 0; index < files.length; index++) {
                const file = files[index];
                const tbook = booksToUpload[index];
                const response = await uploadBook(file);
                if (await response.status === 200) {
                    const newState = this.state.books.map((it) => {
                        if (it === tbook) {
                            return { ...it, uploading: false };
                        }
                        return it;
                    });
                    this.setState({ books: newState });
                } else {
                    const newState = this.state.books.map((it) => {
                        if (it === tbook) {
                            return { ...it, uploading: false, error: "Ошибка загрузки файла" };
                        }
                        return it;
                    });
                    this.setState({ books: newState });
                }
            }
        }
    }

    public render() {
        const { classes } = this.props;

        const books = this.state.books.map((it) =>
            <Grid item>
                <Card className={classnames(classes.preview, { [classes.previewDisabled]: it.uploading })}>
                    <CardActionArea href={`/#/books/${it.id}`}>
                        <CardMedia
                            className={classes.media}
                            image={`/api/v1/books/${it.id}/cover`} />
                        {it.error != null &&
                            <CardContent className={classes.content}>
                                <WarningIcon />
                                {it.error}
                            </CardContent>
                        }
                    </CardActionArea>
                    {it.uploading &&
                        <React.Fragment>
                            {/* <CardActions className={classes.cardToolbar}>
                                <Button size="small" variant="outlined" color="primary">Отменить</Button>
                            </CardActions> */}
                            <LinearProgress className={classes.progress} />
                        </React.Fragment>
                    }
                </Card>
            </Grid>);

        return (
            <Dashboard onFilterChange={this.handleFilterChange}>
                <Toolbar disableGutters={false}>
                    <input
                        id="upload_book"
                        type="file"
                        className={classes.input}
                        onChange={this.handleFileUpload}
                        multiple />
                    <label htmlFor="upload_book">
                        <Button variant="contained" component="span" color="primary">Загрузить</Button>
                    </label>
                </Toolbar>
                <Grid className={classes.grid} container spacing={24}>
                    {books}
                </Grid>
            </Dashboard>
        );
    }
}

export default withStyles(styles)(UploadPage);
