import React, { useState } from "react";
import "./SearchBar.css";
import TweetDisplay from "./TweetDisplay";
import TweetData from "./sample.json";

function SearchBar({ placeholder }) {
    const [userInput, userInputData] = useState([]);
    
    const handleInput = (event) => {
        const input  = event.target.value;
        userInputData(input);
        console.log(input)
    };
    
    return (
        <div className="search">
            <div className="searchInputs">
                <input type="text" placeholder={placeholder} onChange={handleInput} />
                
                <div className='btn'>
                    <button class="button" onClick={handleInput}>Search!</button>
                </div>
            </div>

        
            {userInputData.length != 0 && (
                <TweetDisplay data={TweetData} />
            )}
        </div>
    );

}

export default SearchBar;





