import React from "react";
import "./App.css";
import SearchBar from "./Components/SearchBar";
import TweetDisplay from "./Components/TweetDisplay";

function App() {
  return (
    <div className="App">
      <SearchBar placeholder="Search..." />
    </div>
  );
}

export default App;