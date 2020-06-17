import React from 'react';

class CommentBox1 extends React.Component {

    constructor(props) {
        super(props);
        // state 就先不需要了。
        // this.state = {
        //     value : ""
        // }
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    // handleChange 方法也不需要了。
    // handleChange(event) {
    //     this.setState({
    //         value : event.target.value
    //     })
    // }

    handleSubmit(event) {
        //alert(this.state.value)
        //alert(this.textInput.value);
        this.props.onAddComment(this.textInput.value)
        this.textInput.value = ""
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
                ref={(textInput) => {
                    this.textInput = textInput;
                }}
            />
            <button type="submit" className="btn btn-primary">提交</button>
            </div>
            </form>
            <p>已有 {this.props.commentLenght} 条评论</p>
            </div>
        )
    }
}

export default CommentBox1