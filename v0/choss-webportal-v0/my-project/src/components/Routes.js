import React from 'react';
import { Router, Route, browserHistory, IndexRoute } from '@version/react-router-v3'

import App2 from '../App2'
import Home from './Home'
import About from './About'
import NotFound from './NotFound'

const history = browserHistory;

const Routes = () => (

    <Router history={browserHistory}>
        <Route path="/" component={App2} >
            <IndexRoute component={Home} />
            <Route path="home" component={Home} />
            <Route path="about" component={About} />
            <Route path="*" component={NotFound} />
        </Route>
    </Router>
)

export default Routes;

