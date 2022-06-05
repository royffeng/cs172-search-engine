import React from "react";
import "./App.css";
import SearchBar from "./Components/SearchBar";
import TweetDisplay from "./Components/TweetDisplay";
import TweetData from "./sample.json";

function App() {
  return (
    <div className="App">
      <SearchBar placeholder="Search..." />
      <TweetDisplay data={TweetData} />
    </div>
  );
}

export default App;