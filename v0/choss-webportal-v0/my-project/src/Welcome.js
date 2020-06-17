import React from 'react'
import LikesButton from './components/LikesButton'


class Welcome extends React.Component {
    render() {
        const toDo = ["学习 React", "学习 Java"];
        const isLogin = false;
        const test = <h1> Hello chengchao </h1>
        console.log(test);


        return (
            <div className="hehe" htmlFor="ss">
                <h1>It works</h1>
                { "hello world" }
                
        
                <p >你好</p>
                <LikesButton likes={10} />
                <ul>
                  {toDo.map((item, index) => <li key={index}>{item}</li>)}
                </ul>
            
                { isLogin ? <p>您已经登录</p> : <p>请您登录</p>}
            </div>
        )

    }
}

export default Welcome