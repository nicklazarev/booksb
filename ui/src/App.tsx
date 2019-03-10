import * as React from "react";
import { HashRouter, Route } from "react-router-dom";
import ContentPage from "./components/ContentPage/ContentPage";
import DashboardPage from "./components/DashboardPage/DashboardPage";
import SignIn from "./components/SignIn/SignIn";
import UploadPage from "./components/UploadPage";

interface User {
  username: string;
}

interface AppState {
  currentUser: User | null;
}

class App extends React.Component<any, AppState> {
  constructor(props: any) {
    super(props);
    this.state = {currentUser: null};
  }

  public render() {
    const { currentUser } = this.state;

    return (
      <HashRouter>
        <div>
          <Route exact path="/signin" render={(props) => <SignIn
            {...props}
            onSuccessfulAuth={this.handleAuth}
            />} />
          <Route exact path="/" component={DashboardPage} />
          <Route path="/upload" component={UploadPage} />
          <Route path="/books/:id" component={ContentPage} />
        </div>
      </HashRouter>
    );
  }

  private handleAuth = (username: string) => {
    this.setState({currentUser: { username }});
  }
}

export default App;
