import React from 'react'
import View from './components/View'


const App2 = ({children}) => {
    return (
        <div>
            <View />
            <div>{children}</div>
        </div>
    )
};

export default App2;