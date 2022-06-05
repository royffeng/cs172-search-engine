import React, { useState } from "react";
import "./SearchBar.css";
import { TweetDisplay } from "./TweetDisplay";
function SearchBar({ placeholder }) {
  return (
    <div className="search">
        <div className="searchInputs">
            <input type="text" placeholder={placeholder} />
            <div className="searchIcon"> </div>
        </div>
    </div>
  );
}

export default SearchBar;





