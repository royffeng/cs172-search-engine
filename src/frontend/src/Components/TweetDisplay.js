import React from 'react';
import './TweetDisplay.css'

function TweetDisplay( {data} ) {
    return (
        <div className='TweetDisplay'>
                {data.map((value, key) => {
                    return(
                        <div className='card'>
                            <h5 class="firstLine"> {value.name} </h5> 
                            <a class="linked firstLine" href={value.url} target="_blank"> {value.screen_name} </a>
                            <div class="fadedText firstLine"> {value.created_at} </div>
                            <p class="text"> {value.tweet_text} </p> 
                        </div>
                    )
                })}

        </div>
    );
}

export default TweetDisplay;
