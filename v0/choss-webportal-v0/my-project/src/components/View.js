import React from 'react'
import { Link } from '@version/react-router-v3'


class View extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      liStyle : {color: "blue"}
    }
  }
  render() {
    return (
      <div>
          <ul>
              <li ><Link to="home">Home</Link></li>
              <li ><Link to="about">About</Link></li>
          </ul>
      </div>
    )
  }

}
export default View 