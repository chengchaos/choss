import React from 'react';


import logo from './logo.svg';
import Welcome from './Welcome';

import DigitalClock from './components/DigitalClock';
import CommentBox1 from './components/CommentBox1'
import CommentList from './components/CommentList'

class App extends React.Component {

  constructor(props) {
    super(props)

    this.state = {
      comments : ['Sweeper the floor is just a surface job of mine.',
        "say hai"]
    };

    this.onAddComment = this.onAddComment.bind(this)
  }

  onAddComment(value) {
    this.setState({
      //comments : this.state.comments.concat(value)
      comments : [...this.state.comments, value]
    })
  }
  
  render_001() {
    
    const { comments } = this.state
    return (
      <div className="App">
      <CommentList comments={ comments }/>
      
      <CommentBox1 commentLenght={ comments.length } onAddComment={this.onAddComment}/>
     
      </div>
    )
  }

  render() {
    return (
      
      <div className="alert alert-success">
      <h1>Simple SAP</h1>
      <ul className="header">
      <li>李白</li>
      <li>杜甫</li>
      <li>李商隐</li>
      </ul>
      <a href="/name-card">我的名片</a>
      </div>
      
    )
  }

}

export default App;
