import { createStyles, Theme, WithStyles, withStyles } from "@material-ui/core/styles";
import cx from "classnames";
import * as React from "react";
import { RouteComponentProps } from "react-router-dom";
import Dashboard from "../../components/Dashboard";

const styles = createStyles({
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

interface ContentPageProps extends WithStyles<typeof styles>, RouteComponentProps<RouteInfo> {

}

interface ContentPageState {
    content: string | null;
}

class ContentPage extends React.Component<ContentPageProps, ContentPageState> {
    constructor(props: ContentPageProps) {
        super(props);
        this.state = { content: null };
    }

    public async componentDidMount() {
        const { id } = this.props.match.params;
        const response = await fetch(`/api/v1/books/${id}/view`);
        const text = await response.text();
        this.setState({ content: text });
    }

    public render() {
        const { classes } = this.props;
        const { content } = this.state;

        return <Dashboard>
            <div className={classes.content}>
                <div
                className={cx(classes.page, "page")}
                dangerouslySetInnerHTML={{ __html: content ? content : "" }}></div>
            </div>
        </Dashboard>;
    }
}

export default withStyles(styles)(ContentPage);
