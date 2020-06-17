import React from 'react';

class LikesButton extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            likes : props.likes
        }
        // this.increaseLikes = this.increaseLikes.bind(this)
        // onClick={this.increaseLikes}
        // 或者使用箭头函数。onClick={() => this.increaseLikes()}
    }

    increaseLikes() {
        this.setState({
            likes: ++this.state.likes
        })
    }
    render() {
        return (
            <div className="likes-button-component">
                <button type="button" className="btn btn-outline-primary btn-lg"
                onClick={() => this.increaseLikes()}
                >:likes: {this.state.likes}


                </button>
            </div>
        )
    }
}

export default LikesButton