import { createStyles, Theme, WithStyles, withStyles } from "@material-ui/core/styles";
import cx from "classnames";
import * as React from "react";
import { RouteComponentProps } from "react-router-dom";
import Dashboard from "../../components/Dashboard";

const styles = createStyles({
    area: {
        display: "flex",
        flexDirection: "column"
    },
    content: {
        display: "flex",
        justifyContent: "center",
    },
    page: {
        "& img": {
            maxWidth: "100%",
        },
        "width": "80ch",
    },
});

interface RouteInfo {
    id: string;
}

interface Page {
    title: string,
    link: string,
}

interface ContentPageProps extends WithStyles<typeof styles>, RouteComponentProps<RouteInfo> {

}

interface ContentPageState {
    content: string | null;
    toc: Page[]
}

class ContentPage extends React.Component<ContentPageProps, ContentPageState> {
    constructor(props: ContentPageProps) {
        super(props);
        this.state = { toc: [], content: null };
    }

    public async componentDidMount() {
        const { id } = this.props.match.params;
        const tocResponse = await fetch(`/api/v1/books/${id}`);
        const book = await tocResponse.json();
        const response = await fetch(`/api/v1/books/${id}/view`);
        const text = await response.text();
        this.setState({ toc: book.contents, content: text });
    }

    public render() {
        const { classes } = this.props;
        const { content, toc } = this.state;

        return <Dashboard>
            <div className={classes.content}>
                <div className={classes.area}>
                    <ul>
                        {toc.map(it => (<li><a href={it.link}>{it.title}</a></li>))}
                    </ul>
                    <div
                        className={cx(classes.page, "page")}
                        dangerouslySetInnerHTML={{ __html: content ? content : "" }}></div>
                </div>
            </div>
        </Dashboard>;
    }
}

export default withStyles(styles)(ContentPage);
