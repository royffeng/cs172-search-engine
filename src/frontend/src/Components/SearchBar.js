import React, { useState } from "react";
import "./SearchBar.css";
import TweetDisplay from "./TweetDisplay";
import TweetData from "./sample.json";
import axios from 'axios';

const SEARCH_SUBMIT_API = 'http://localhost:8080/api/search?queryString='

function SearchBar({ placeholder }) {
    const [userInput, setUserInput] = useState("");
    const [searchResults, setSearchResults] = useState({})

    const handleInput = async (e) => {
        setUserInput(e.target.value)
        console.log(userInput)
    }

    const searchSubmit = async (e) => {
        e.preventDefault()
        // checking what user input is being sent over to the backend
        console.log(userInput)
        console.log(axios.post(SEARCH_SUBMIT_API, userInput))
        axios.post(SEARCH_SUBMIT_API, userInput)
            .then(resAxios => {
                setSearchResults(resAxios.data)
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

            <TweetDisplay data={TweetData} />
        </div>
    );
}

export default SearchBar;
