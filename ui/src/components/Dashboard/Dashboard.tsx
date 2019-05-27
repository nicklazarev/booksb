import {ListItem, ListItemIcon, ListItemText,} from "@material-ui/core";
import AppBar from "@material-ui/core/AppBar";
import CssBaseline from "@material-ui/core/CssBaseline";
import Divider from "@material-ui/core/Divider";
import Drawer from "@material-ui/core/Drawer";
import IconButton from "@material-ui/core/IconButton";
import InputBase from "@material-ui/core/InputBase";
import List from "@material-ui/core/List";
import {createStyles, Theme, WithStyles, withStyles} from "@material-ui/core/styles";
import {fade} from "@material-ui/core/styles/colorManipulator";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import ChevronLeftIcon from "@material-ui/icons/ChevronLeft";
import UploadIcon from "@material-ui/icons/CloudUpload";
import InboxIcon from "@material-ui/icons/Inbox";
import MenuIcon from "@material-ui/icons/Menu";
import SearchIcon from "@material-ui/icons/Search";
import classNames from "classnames";
import * as React from "react";
import {withRouter} from "react-router-dom";

const drawerWidth = 240;

function styles(theme: Theme) {
    return createStyles({
        appBar: {
            transition: theme.transitions.create(["width", "margin"], {
                duration: theme.transitions.duration.leavingScreen,
                easing: theme.transitions.easing.sharp,
            }),
            zIndex: theme.zIndex.drawer + 1,
        },
        appBarShift: {
            marginLeft: drawerWidth,
            transition: theme.transitions.create(["width", "margin"], {
                duration: theme.transitions.duration.enteringScreen,
                easing: theme.transitions.easing.sharp,
            }),
            width: `calc(100% - ${drawerWidth}px)`,
        },
        appBarSpacer: theme.mixins.toolbar,
        chartContainer: {
            marginLeft: -22,
        },
        content: {
            flexGrow: 1,
            height: "100vh",
            overflow: "auto",
        },
        drawerPaper: {
            position: "relative",
            transition: theme.transitions.create("width", {
                duration: theme.transitions.duration.enteringScreen,
                easing: theme.transitions.easing.sharp,
            }),
            whiteSpace: "nowrap",
            width: drawerWidth,
        },
        drawerPaperClose: {
            overflowX: "hidden",
            transition: theme.transitions.create("width", {
                duration: theme.transitions.duration.leavingScreen,
                easing: theme.transitions.easing.sharp,
            }),
            width: theme.spacing.unit * 7,
            [theme.breakpoints.up("sm")]: {
                width: theme.spacing.unit * 9,
            },
        },
        glossary: {
            width: "40vw",
        },
        h5: {
            marginBottom: theme.spacing.unit * 2,
        },
        inputInput: {
            paddingBottom: theme.spacing.unit,
            paddingLeft: theme.spacing.unit * 10,
            paddingRight: theme.spacing.unit,
            paddingTop: theme.spacing.unit,
            transition: theme.transitions.create("width"),
            width: "100%",
            [theme.breakpoints.up("sm")]: {
                "&:focus": {
                    width: 500,
                },
                "width": 200,
            },
        },
        inputRoot: {
            color: "inherit",
            width: "100%",
        },
        menuButton: {
            marginLeft: 12,
            marginRight: 36,
        },
        menuButtonHidden: {
            display: "none",
        },
        root: {
            display: "flex",
        },
        search: {
            "&:hover": {
                backgroundColor: fade(theme.palette.common.white, 0.25),
            },
            "backgroundColor": fade(theme.palette.common.white, 0.15),
            "borderRadius": theme.shape.borderRadius,
            "marginLeft": 0,
            "position": "relative",
            "width": "100%",
            [theme.breakpoints.up("sm")]: {
                marginLeft: theme.spacing.unit,
                width: "auto",
            },
        },
        searchIcon: {
            alignItems: "center",
            display: "flex",
            height: "100%",
            justifyContent: "center",
            pointerEvents: "none",
            position: "absolute",
            width: theme.spacing.unit * 9,
        },
        tableContainer: {
            height: 320,
        },
        title: {
            flexGrow: 1,
        },
        toolbar: {
            paddingRight: 24, // keep right padding when drawer closed
        },
        toolbarIcon: {
            alignItems: "center",
            display: "flex",
            justifyContent: "flex-end",
            padding: "0 8px",
            ...theme.mixins.toolbar,
        },
    });
}

interface DashboardProps extends WithStyles<typeof styles> {
    onFilterChange?: (filer: string) => void;
}

class Dashboard extends React.Component<DashboardProps> {
    public state = {
        open: true,
        openMenu: false,
    };

    public anchorEl = null;

    public handleDrawerOpen = () => {
        this.setState({ open: true });
    };

    public handleDrawerClose = () => {
        this.setState({ open: false });
    };

    public handleFilterChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { onFilterChange } = this.props;
        if (onFilterChange != null) {
            onFilterChange(event.currentTarget.value);
        }
    };

    public render() {
        const { classes, children } = this.props;

        const BooksLink = withRouter(({ history }) => (
            <ListItem button onClick={() => { history.push("/"); }}>
                <ListItemIcon><InboxIcon /></ListItemIcon>
                <ListItemText>Все Книги</ListItemText>
            </ListItem>
        ));

        const UploadLink = withRouter(({ history }) => (
            <ListItem button onClick={() => { history.push("/upload"); }}>
                <ListItemIcon><UploadIcon /></ListItemIcon>
                <ListItemText>Загрузить</ListItemText>
            </ListItem>
        ));

        return (
            <div className={classes.root}>
                <CssBaseline />
                <AppBar
                    position="absolute"
                    className={classNames(classes.appBar, this.state.open && classes.appBarShift)}
                >
                    <Toolbar disableGutters={!this.state.open} className={classes.toolbar}>
                        <IconButton
                            color="inherit"
                            aria-label="Open drawer"
                            onClick={this.handleDrawerOpen}
                            className={classNames(
                                classes.menuButton,
                                this.state.open && classes.menuButtonHidden,
                            )}
                        >
                            <MenuIcon />
                        </IconButton>
                        <Typography
                            component="h1"
                            variant="h6"
                            color="inherit"
                            noWrap
                            className={classes.title}
                        >
                            BooksB
                        </Typography>
                        <div className={classes.search}>
                            <div className={classes.searchIcon}>
                                <SearchIcon />
                            </div>
                            <InputBase
                                placeholder="Search…"
                                classes={{
                                    input: classes.inputInput,
                                    root: classes.inputRoot,
                                }}
                                onChange={this.handleFilterChange}
                            />
                        </div>
                    </Toolbar>
                </AppBar>
                <Drawer
                    variant="permanent"
                    classes={{
                        paper: classNames(classes.drawerPaper, !this.state.open && classes.drawerPaperClose),
                    }}
                    open={this.state.open}
                >
                    <div className={classes.toolbarIcon}>
                        <IconButton onClick={this.handleDrawerClose}>
                            <ChevronLeftIcon />
                        </IconButton>
                    </div>
                    <Divider />
                    <List>
                        <BooksLink />
                        <ListItem button>
                            <ListItemIcon><InboxIcon /></ListItemIcon>
                            <ListItemText>Категории</ListItemText>
                        </ListItem>
                        <ListItem button>
                            <ListItemIcon><InboxIcon /></ListItemIcon>
                            <ListItemText>Мои списки книг</ListItemText>
                        </ListItem>
                        <ListItem button>
                            <ListItemIcon><InboxIcon /></ListItemIcon>
                            <ListItemText>Мои заметки</ListItemText>
                        </ListItem>
                        <ListItem button>
                            <ListItemIcon><InboxIcon /></ListItemIcon>
                            <ListItemText>Фрагменты</ListItemText>
                        </ListItem>
                    </List>
                    <Divider />
                    <List>
                        <UploadLink />
                    </List>
                </Drawer>
                <main className={classes.content}>
                    <div className={classes.appBarSpacer} />
                    {children}
                </main>
            </div>
        );
    }
}

export default withStyles(styles)(Dashboard);
