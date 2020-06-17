import React from 'react';

class CommentBox extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            value : ""
        }
        this.handleChange = this.handleChange.bind(this)
        this.handleSubmit = this.handleSubmit.bind(this)
    }

    handleChange(event) {
        this.setState({
            value : event.target.value
        })
    }

    handleSubmit(event) {
        alert(this.state.value)
        event.preventDefault()
    }

    render() {
        return (
            <div>
            <form className="p-5" onSubmit={this.handleSubmit}>
            <div className="form-group">
            <label>留言内容：</label>
            <input
                type="text" className="form-control"
                placeholder="请输入"
                onChange={this.handleChange}
                value={this.state.value}
            />
            <button type="submit" className="btn btn-primary">提交</button>
            </div>
            </form>
            </div>
        )
    }
}

export default CommentBox