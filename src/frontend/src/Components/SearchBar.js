import React, { useState } from "react";
import "./SearchBar.css";
import TweetDisplay from "./TweetDisplay";
import TweetData from "./sample.json";
import axios from 'axios';

const SEARCH_SUBMIT_API = 'http://localhost:8080/api/search/'

function SearchBar({ placeholder }) {
    const [userInput, setUserInput] = useState("");
    const [searchResults, setSearchResults] = useState([]);

    const handleInput = async (e) => {
        setUserInput(e.target.value)
        console.log(userInput)
    }

    const searchSubmit = async (e) => {
        e.preventDefault()
        // checking what user input is being sent over to the backend
        console.log(userInput)

        axios.post(SEARCH_SUBMIT_API, userInput)
            .then(resAxios => {
                setSearchResults(resAxios.data)
                // printing out results from the backend, should work once mongo is connected
                console.log(searchResults)
            })
            .catch(err => {
                alert("ERROR: " + err)
            })
    }

        return (
            <div className="search">
                <div className="searchInputs">
                    <input type="text" placeholder={placeholder} onChange={handleInput} />

                    <div className='btn'>
                        <button class="button" onClick={searchSubmit}>Search!</button>
                    </div>
                </div>
                {/*this is a comment: switched TweetData for searchResults, frontend page is now blank*/}
                <TweetDisplay data={searchResults} />
            </div>
        );
}

export default SearchBar;

