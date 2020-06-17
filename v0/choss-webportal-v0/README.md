

## 准备开发环境

- 官方脚手架工具 create-react-app

https://github.com/facebook/create-react-app

1, 安装 node （ 版本大于 6.0 ）

```bash
$ node -v
v10.16.0
$ npm install create-react-app -g
```


**用法： `create-react-app <my-project 项目名称>`**

```bash
$ create-react-app
Please specify the project directory:
  create-react-app <project-directory>

For example:
  create-react-app my-react-app

Run create-react-app --help to see all options.
$ create-react-app --help
Usage: create-react-app <project-directory> [options]

Options:

  -V, --version                            output the version number
  --verbose                                print additional logs
  --info                                   print environment debug info
  --scripts-version <alternative-package>  use a non-standard version of react-scripts
  --use-npm
  -h, --help                               output usage information
    Only <project-directory> is required.

    A custom --scripts-version can be one of:
      - a specific npm version: 0.8.2
      - a custom fork published on npm: my-react-scripts
      - a .tgz archive: https://mysite.com/my-react-scripts-0.8.2.tgz
      - a .tar.gz archive: https://mysite.com/my-react-scripts-0.8.2.tar.gz
    It is not needed unless you specifically want to use a fork.

    If you have any problems, do not hesitate to file an issue:
      https://github.com/facebookincubator/create-react-app/issues/new

...

Success! Created my-project at /Users/chengchao/git/github/choss/choss-webportal-v0/my-project
Inside that directory, you can run several commands:

  npm start
    Starts the development server.

  npm run build
    Bundles the app into static files for production.

  npm test
    Starts the test runner.

  npm run eject
    Removes this tool and copies build dependencies, configuration files
    and scripts into the app directory. If you do this, you can’t go back!

We suggest that you begin by typing:

  cd my-project
  npm start

Happy hacking!

```



```js
import React from 'react'

class Welcome extends React.Component {
    render() {
        return <h1>Hello</h1>
    }
}

export default Welcome
```

### 第一个组建的总结

- 导入 React
- 创建类继承自 React.Component
- render() 方法
- ReactDOM.render() 方法


### JSX 会编译成什么？

- JSX 是一种语法糖 `React.createElement()`
- 会编译成 ReactElement 对象。

https://babeljs.io/


```jsx
<h1>Hello React</h1>

// 
React.createElement(
  "h1",
  null,
  "Hello React"
)

<h1 className="test">Hello React</h1>

// 
React.createElement(
  "h1",
  { className : "test"},
  "Hello React"
)


<h1 className="test">Hello React<span>test</span></h1>

// 
React.createElement(
  "h1",
  { className : "test"},
  "Hello React",
  React.createElement(
    "span",
    null,
    "test"
  )
)


// 自定义 Component 第一个字母要大些
<Welcome />

//
React.createElement(Welcome, null);

```


## Props/State/Forms 属性和状态

安装 bootstrap 样式：

```bash
$ npm install bootstrap --save
```

引入：

```jsx
import 'bootstrap/dist/css/bootstrap.min.css'
```


**Props （属性）是只读的 ！！！**

**State （状态）是可以改变的**

只能使用一种方式改变： `this.setState()` 

### 生命周期

- 组建初始化
- 组建更新
- 组建卸载

