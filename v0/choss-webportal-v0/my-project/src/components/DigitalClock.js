import React from 'react'

class DigitalClock extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            date : new Date()
        }
    }

    componentDidMount() {
        this.timer = setInterval(() => {
            this.setState({
                date : new Date()
            })
        }, 3000);
    }

    componentDidUpdate(currentProps, currentState) {

        console.log("current state =", currentProps, currentState)

    }
    componentWillMount() {
        clearInterval(this.timer)
    }
    render() {
        return (
            <div className="digital-clock-component jumbotrom">
            <h1>{this.state.date.toLocaleString()}</h1>
            </div>
        )
    }
}

export default DigitalClock