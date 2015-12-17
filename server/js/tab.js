function getSelectableHTML(pseudo, email, gcm_regid){
    var html = '<a class="label label-default label-circle inset label-hover list-group-option remove" onclick="deleteUser('+email+');">'
                +'<i class="glyphicon glyphicon-remove"></i>'
            +'</a>'
            +'<a class="list-group-item selectable" mail="'+email+'" regID="'+gcm_regid+'" onclick="return false;">'
               +'<div class="row">'
                    +'<div class="col-sm-4">'
                        +'<span class="label label-default inset">'
                            +'<i class="glyphicon glyphicon-user"></i>'
                            +'<i class="glyphicon glyphicon-ok invisible"></i>'
                        +'</span> '+pseudo
                    +'</div>'
                    +'<div class="col-sm-4">'
                        +email
                    +'</div>'
                    +'<div class="col-sm-4">'
                        +gcm_regid
                    +'</div>'
                +'</div>'
            +'</a>';
    
    return html;
}

function getRowHTML(text){
    var html = '<a class="list-group-item" onclick="return false;">'
               +'<div class="row">'
                    +'<div class="col-sm-12">'
                        +text+'aucun résultat'
                    +'</div>'
                +'</div>'
            +'</a>';

    return html;
}

function addSelectable(list_group, pseudo, email, gcm_regid){
    
    var select_list = $(list_group).find('.select-list');
    select_list.append(getSelectableHTML(pseudo, email, gcm_regid));
    var new_item = select_list.find('.selectable').last();
    new_item.click(function(e){selectable_click(this)});
    
    
    var ondelete = function(e){
        removeword2('regid_list',gcm_regid);
        toggleSelectable(new_item);
    };
    console.log('setOnDelete('+email+',\'user_list\','+ondelete+');');
    setOnDelete(email, 'user_list', ondelete);
    
    
    return new_item;
}

function addRow(list_group, text){
    
    var select_list = $(list_group).find('.select-list');
    var new_item = select_list.append(getRowHTML(text));
    
    return new_item;
}

function reset(list_group){
    $(list_group).find('.select-list').html("");
}

function toggleSelectable(obj){
    
    var label = $(obj).find('.label');
    var options = $(obj).prev('.list-group-options').find('a');
    var selected = $(obj).find('i:last-child');
    
    toggleClass(obj, 'active');
    toggleClass(label, 'label-default');
    toggleClass(label, 'label-primary');
    toggleClass(options, 'label-default');
    toggleClass(options, 'label-primary');
    toggleClass(selected, 'invisible');
};

function selectable_click(obj) {
    
    toggleSelectable(obj);

    var mail = $(obj).attr('mail');
    var registrationId = $(obj).attr('regID');


    if (!existInList(mail, 'user_list')) {
        var ondelete = function(e){
            removeword2('regid_list',registrationId);
            toggleSelectable(obj);
        };
        //add_to_list(mail, 'user_list', ondelete);
        add_to_list(mail, 'user_list', ondelete);
        add_to_list(registrationId, 'regid_list');
    } else {
        removeword('user_list', mail, filterMail(mail));
        removeword2('regid_list', registrationId);
    }
}

$(document).ready(function ()	{
  $('.list-group-item.selectable').click(function(e){selectable_click(this)});
      
})