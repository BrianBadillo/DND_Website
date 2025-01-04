import React from "react";
import DisplayCharacters from "./components/DisplayCharacters";

const App = () => {
    return (
        <div>
            <h1>Welcome to the D&D Item Manager
            </h1>
            <DisplayCharacters userId={12} />
        </div>
    );
};

export default App;
