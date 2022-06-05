import React from 'react';
import './TweetDisplay.css'

function TweetDisplay( {data} ) {
    return (
        <div className='TweetDisplay'>
                {data.map((value, key) => {
                    return(
                        <div className='card'>
                            <h5 class="firstLine"> {value.name} </h5> 
                            <div class="fadedText firstLine"> {value.screen_name} </div>
                            <div class="fadedText firstLine"> {value.created_at} </div>
                            <p class="text"> {value.tweet_text} </p> 
                        </div>
                    )
                })}

        </div>
    );
}

export default TweetDisplay;