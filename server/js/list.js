var lists = {};
var launching = false;

function getList(name){
    
    if (lists[name]) {
        return lists[name];
    }
    
    return null;
}

function removeword(list, value, id) {

    $("#" + list + " #" + id + "container").remove();

    lists[list].splice(lists[list].indexOf(value), 1);

    if (lists[list].length == 0) {
        delete lists[list];
    }

    console.log(lists[list]);

}

function add_to_list_input(input, list, char) {

    var value = $('#' + input).val();

    if (value != "null") {


        var filtered_value;

        var reg = new RegExp("[ ]+", "g");
        var reg3 = new RegExp("#+", "g");
        var reg4 = new RegExp("@+", "g");

        value = value.replace(reg, ' ');

        if (char == "#") {
            //remplace les caractère non voulu---------
            /*var reg = new RegExp("[^a-zA-Z 0-9 ]+","g");
            var reg2 = new RegExp("[ ]+","g");
            value = value.replace(reg, '');
            value = value.replace(reg2, '');*/
            /*var reg2= new RegExp("^#","g");*/

            filtered_value = value.replace(reg3, 'hashtag_');

            /* filtered_value = value.replace(reg2, 'hashtag_');
             filtered_value = filtered_value.replace(reg3, '');*/

        } else if (char == "@") {

            value = value.replace(reg, '');

            //console.log(value.substring(0,1));
            if (value.substring(0, 1) != "@") {
                value = "@" + value
            }

            filtered_value = value.replace(reg4, 'to_');

        } else {
            value = value.replace(reg, '');
            filtered_value = value;

        }

        filtered_value = filtered_value.replace(reg, '_');

        //console.log(value);
        //console.log(filtered_value);
        //-----------------------------------------
        var notempty = value.length && value != " ";
        var exist = $("#" + list + " #" + filtered_value + "container").length;


        if (!exist && notempty) {
            var cont = " <span class='label label-primary inset' id='" + filtered_value + "container'>" + value + "<span id='" + filtered_value +
                "remove' onclick=\"removeword('" + list + "','" + filtered_value + "'); \" class='remove'> x </span></span>";

            //$('#'+input).val("");
            $('#' + list).append($(cont).hide());

            $('#' + list + ' #' + filtered_value + 'container').slideDown(1);

            if (!(list in lists)) {

                lists[list] = [];
            }

            lists[list].push(value);

            //console.log(lists[list]);
        }

    }
}

function existInList(value, list) {
    if (list in lists) {
        //return $("#"+list+" #"+filtered_value+"container").length > 0 ;
        return lists[list].indexOf(value) != -1;
    }

    return false;

}

function add_to_list(value, list) {

    if (value != "null") {


        var filtered_value = filterMail(value);
        //-----------------------------------------
        var notempty = value.length && value != " ";
        var exist = existInList(value, list);


        if (!exist && notempty) {
            var cont = " <span class='label label-primary inset' id='" + filtered_value + "container'>" + value + "<span id='" + filtered_value +
                "remove' onclick=\"removeword('" + list + "','" + value + "','" + filtered_value + "'); \" class='glyphicon glyphicon-remove remove'></span></span>";

            //$('#'+input).val("");
            $('#' + list).append($(cont).hide());

            $('#' + list + ' #' + filtered_value + 'container').slideDown(1);

            if (!(list in lists)) {

                lists[list] = [];
            }

            lists[list].push(value);

            console.log(lists[list]);
        }

    }
}


function filterMail(value) {

    if (value != "null") {
        var filtered_value;

        var reg = new RegExp("[ ]+", "g");
        var reg3 = new RegExp("#+", "g");
        var reg4 = new RegExp("@+", "g");
        var reg5 = new RegExp("[.]+", "g");

        value = value.replace(reg, ' ');
        filtered_value = value;
        filtered_value = filtered_value.replace(reg4, '_');
        filtered_value = filtered_value.replace(reg5, '_');
        console.log(value);
        console.log(filtered_value);

        return filtered_value;
    }
}