import { createStyles, Theme } from "@material-ui/core";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import Checkbox from "@material-ui/core/Checkbox";
import CssBaseline from "@material-ui/core/CssBaseline";
import FormControl from "@material-ui/core/FormControl";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import Paper from "@material-ui/core/Paper";
import { WithStyles, withStyles } from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import * as React from "react";
import { RouteComponentProps } from "react-router-dom";

const styles = (theme: Theme) => createStyles({
    avatar: {
        backgroundColor: theme.palette.secondary.main,
        margin: theme.spacing.unit,
    },
    form: {
        // Fix IE 11 issue.
        marginTop: theme.spacing.unit,
        width: "100%",
    },
    main: {
        [theme.breakpoints.up(400 + theme.spacing.unit * 3 * 2)]: {
            marginLeft: "auto",
            marginRight: "auto",
            width: 400,
        },
        display: "block",
        // Fix IE 11 issue.
        marginLeft: theme.spacing.unit * 3,
        marginRight: theme.spacing.unit * 3,
        width: "auto",
    },
    paper: {
        alignItems: "center",
        display: "flex",
        flexDirection: "column",
        marginTop: theme.spacing.unit * 8,
        padding: `${theme.spacing.unit * 2}px ${theme.spacing.unit * 3}px ${theme.spacing.unit * 3}px`,
    },
    submit: {
        marginTop: theme.spacing.unit * 3,
    },
});

interface SignInProps extends WithStyles<typeof styles>, RouteComponentProps<any> {
    onSuccessfulAuth: (username: string) => void;
}

function SignIn(props: SignInProps) {
    const { classes } = props;

    const rawLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        const formData = new FormData(e.target as HTMLFormElement);
        const data = { email: formData.get("email") as string, password: formData.get("password") as string };
        const headers = new Headers();
        headers.append("Content-Type", "application/json");
        const response = await fetch(`/api/v1/login`, { method: "POST", headers, body: JSON.stringify(data) });
        const result = response.status === 200;
        console.log(`auth: ${result}`);
        if (result) {
            props.onSuccessfulAuth(data.email);
            props.history.push("/");
        }
    };

    return (
        <main className={classes.main}>
            <CssBaseline />
            <Paper className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <LockOutlinedIcon />
                </Avatar>
                <Typography component="h1" variant="h5">
                    Sign in
                </Typography>
                <form className={classes.form} onSubmit={rawLogin}>
                    <FormControl margin="normal" required fullWidth>
                        <InputLabel htmlFor="email">Email Address</InputLabel>
                        <Input id="email" name="email" autoComplete="email" autoFocus />
                    </FormControl>
                    <FormControl margin="normal" required fullWidth>
                        <InputLabel htmlFor="password">Password</InputLabel>
                        <Input name="password" type="password" id="password" autoComplete="current-password" />
                    </FormControl>
                    <FormControlLabel
                        control={<Checkbox value="remember" color="primary" />}
                        label="Remember me"
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        className={classes.submit}
                    >
                        Sign in
                    </Button>
                </form>
            </Paper>
        </main>
    );
}

export default withStyles(styles)(SignIn);
