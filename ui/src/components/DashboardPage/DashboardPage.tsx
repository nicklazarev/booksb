import { Card, CardActionArea, CardContent, CardMedia, Grid } from "@material-ui/core";
import { createStyles, Theme, WithStyles, withStyles } from "@material-ui/core/styles";
import { WarningOutlined as WarningIcon } from "@material-ui/icons";
import classnames from "classnames";
import { debounce } from "debounce";
import * as React from "react";
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

interface DashboardPageProps extends WithStyles<typeof styles> {

}

interface Book {
    id: string | null;
    isbn: string | null;
    uploading: boolean | null;
    cover: string | null;
    error: string | null;
}

interface DashboardPageState {
    books: Book[];
}

class DashboardPage extends React.Component<DashboardPageProps, DashboardPageState> {

    public handleFilterChange = debounce((filter: string | null) => {
        this.fetchBooks(filter);
    }, 300);
    constructor(props: DashboardPageProps) {
        super(props);
        this.state = { books: [] };
    }

    public fetchBooks = async (filter?: string | null) => {
        const response = await fetch(`/api/v1/books?name=${filter || ""}`);
        if (response.status == 200) {
            const books = await response.json();
            this.setState({ books });
        }
    }

    public componentDidMount() {
        this.fetchBooks();
    }

    public render() {
        const { classes } = this.props;

        const books = this.state.books.map((it) =>
            <Grid item>
                <Card className={classnames(classes.preview, { [classes.previewDisabled]: it.uploading })}>
                    <CardActionArea href={`/#/books/${it.id}`}>
                        <CardMedia className={classes.media} image={`/api/v1/books/${it.id}/cover`} />
                        {it.error != null &&
                            <CardContent className={classes.content}>
                                <WarningIcon />
                                {it.error}
                            </CardContent>
                        }
                    </CardActionArea>
                </Card>
            </Grid>);

        return (
            <Dashboard onFilterChange={this.handleFilterChange}>
                <Grid className={classes.grid} container spacing={24}>
                    {books}
                </Grid>
            </Dashboard>
        );
    }
}

export default withStyles(styles)(DashboardPage);
