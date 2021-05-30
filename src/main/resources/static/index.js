window.onload = function() {
    var refresherButton = document.getElementById('iframeRefresher');
    if (refresherButton.addEventListener) {
        refresherButton.addEventListener('click', refreshIframe, false);
    }
    else {
        refresherButton.attachEvent('click', refreshIframe);
    }

    
    if(!document.queryCommandSupported("undo")){
        document.getElementById('undo').disabled = true;
    }
    if(!document.queryCommandSupported("redo")){
        document.getElementById('redo').disabled = true;
    }

    document.getElementById("bold").addEventListener('click', function(event){
        event.preventDefault();
        console.log(this);
        /* Do some other things*/
    });
}

function refreshIframe() {
    var roll = "/roll?";

    //update side
    const diceRegex = new RegExp('^d(\\d+)$');
    var dice = document.getElementById('dice').value;
    if(dice == ""){
        alert("Dice input is required.");
        return;
    }
    if (!diceRegex.test(dice)){
        alert("Must be formatted as d#.");
        return;
    }
    dice = dice.substring(1, dice.length);
    roll += "s=" + dice;

    //update num
    const numRegex = new RegExp('^(\\d*)$');
    var num = document.getElementById('quantity').value;
    if(num != ""){
        if (!numRegex.test(num)){
            alert("Quantity must only contain numbers.");
            return;
        }
        roll += "&n=" + num;
    }

    //update mod
    const modRegex = new RegExp('^(\\d*)$');
    var mod = document.getElementById('mod').value;
    if(mod != ""){
        if (!modRegex.test(mod)){
            alert("Modifier must only contain numbers.");
            return;
        }
        roll += "&m=" + mod;
    }

    //update advantage
    var adv = document.getElementById('adv').checked;
    var dis = document.getElementById('dis').checked;
    var a = 0;
    if(adv) a += 1;
    if(dis) a -= 1;
    roll += "&a=" + a;

    //update explosion
    if(!adv && !dis){
        const expRegex = new RegExp('^(\\d*)$');
        var exp = document.getElementById('exp').value;
        var expAbove = document.getElementById('expAbove').checked ? 1 : 0;
        if(exp != ""){
            if (!expRegex.test(exp)){
                alert("Explosion must only contain numbers.");
                return;
            }
            roll += "&ep=" + exp + "&ea=" + expAbove;
        }
    }

    //update dc
    const dcRegex = new RegExp('^(\\d*)$');
    var dc = document.getElementById('dc').value;
    if(dc != ""){
        if (!dcRegex.test(dc)){
            alert("DC must only contain numbers.");
            return;
        }
        roll += "&dc=" + dc;
    }

    //reload iFrame
    document.getElementById('inlineFrameExample').src = roll;
}

function disableExplosion(checkbox){
    var adv = document.getElementById('adv').checked;
    var dis = document.getElementById('dis').checked;

    if (adv || dis){
        document.getElementById('exp').disabled=true;
        document.getElementById('expAbove').disabled=true;
    } else {
        document.getElementById('exp').disabled=false;
        document.getElementById('expAbove').disabled=false;
    }
}

function updateButton(button){
    if(button.classList.contains("active")){
        button.classList.remove("active");
    } else {
        button.classList.add("active");
    }
}

function enableButton(button){
    if(button.classList.contains("active")){
        button.classList.remove("active");
    } else {
        button.classList.add("active");
    }
}