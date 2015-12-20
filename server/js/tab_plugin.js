(function ($) {

    $.fn.myTab = function (options) {

        var defauts = {
            "vitesseFadeOut": "slow",
            "vitesseFadeIn": "fast"
        };
        var parametres = $.extend(defauts, options);
        var element = $(this);

        var objects = {
            rows: null,
            select_list: null,
            list: null
        }

        var params = {
            intervals: 0
        }

        function initTab() {
            element.html('');
            element.append('<div class="list-group">'
                +'<a class="list-group-item list-group-action" onclick="$(\'#panel-users\').toggleClass(\'visible\'); return false;">'
                    +'<span class="label label-primary inset">'
                        +'<i class="glyphicon glyphicon-plus"></i>'
                    +'</span> envoyer une notification'
                +'</a>'
                +'<div class="list-group-item list-group-title" >'
                    +'<div class="row">'
                        +'<div class="col-sm-4">'
                            +'pseudo'
                        +'</div>'
                        +'<div class="col-sm-4">'
                            +'mail'
                        +'</div>'
                        +'<div class="col-sm-4">'
                            +'registrationID'
                        +'</div>'
                    +'</div>'
                +'</div>'
                +'<span class="select-list">'
                +'</span>'
            +'</div>');
                           
            objects.rows = [];
            objects.select_list = element.find('.select-list');
            objects.list = $(parametres.list).myList();
        }

        //var list = $('#user_list').myList();

        function getSelectableHTML(pseudo, email, gcm_regid) {
            var html = '<a class="label label-default label-circle inset label-hover list-group-option remove" onclick="deleteUser(' + email + ');">' 
                + '<i class="glyphicon glyphicon-remove"></i>' 
            + '</a>' 
            + '<a class="list-group-item selectable" mail="' + email + '" regID="' + gcm_regid + '" onclick="return false;">' 
                + '<div class="row">' 
                    + '<div class="col-sm-4">' 
                        + '<span class="label label-default inset">' 
                            + '<i class="glyphicon glyphicon-user"></i>' 
                            + '<i class="glyphicon glyphicon-ok invisible"></i>' 
                        + '</span>' 
                        + pseudo 
                    + '</div>' 
                    + '<div class="col-sm-4">' 
                        + email 
                    + '</div>' 
                    + '<div class="col-sm-4">' 
                        + gcm_regid 
                    + '</div>' 
                + '</div>' 
            + '</a>';

            return html;
        }

        function getRowHTML(text) {
            var html = '<a class="list-group-item" onclick="return false;">' 
                + '<div class="row">' 
                    + '<div class="col-sm-12">' 
                        + text 
                    + '</div>' 
                + '</div>' 
            + '</a>';

            return html;
        }

        function addSelectable(pseudo, email, gcm_regid) {

            var select_list = element.find('.select-list');
            select_list.append(getSelectableHTML(pseudo, email, gcm_regid));
            var new_item = select_list.find('.selectable').last();
            new_item.click(function (e) {
                selectable_click(this)
            });
            
            if(objects.list != null){
                var ondelete = function (e) {
                    toggleSelectable(new_item);
                };
                //console.log('setOnDelete('+email+',\'user_list\','+ondelete+');');
                objects.list.onDelete(email, ondelete);
            }

            return new_item;
        }

        function addRow(text) {

            var select_list = element.find('.select-list');
            var new_item = select_list.append(getRowHTML(text));

            return new_item;
        }

        function reset() {
            element.find('.select-list').html("");
        }

        function toggleSelectable(selectable) {

            var label = $(selectable).find('.label');
            var options = $(selectable).prev('.list-group-options').find('a');
            var selected = $(selectable).find('i:last-child');

            toggleClass(selectable, 'active');
            toggleClass(label, 'label-default');
            toggleClass(label, 'label-primary');
            toggleClass(options, 'label-default');
            toggleClass(options, 'label-primary');
            toggleClass(selected, 'invisible');

            console.log('toggle');
        };

        function selectable_click(selectable) {

            toggleSelectable(selectable);
            
            if(objects.list != null){
                var pseudo = $(selectable).attr('pseudo');
                var mail = $(selectable).attr('mail');
                var registrationId = $(selectable).attr('regID');
                
                if (!objects.list.valueExist(registrationId)) {
                    var ondelete = function (e) {
                        toggleSelectable(selectable);
                    };
                    objects.list.addValue(registrationId, mail, ondelete);
                } else {
                    objects.list.delValue(registrationId);
                }
            }
            
        }

        initTab();

        return {
            getValues: function () {
                return objects.list.getValues();
            },
            toggleSelectable: function (selectable) {
                toggleSelectable(selectable);
            },
            addRow: function (pseudo, email, gcm_regid) {
                return addSelectable(pseudo, email, gcm_regid);
            },
            addMessageRow: function (text) {
                return addRow(text);
            },
            reset: function () {
                element.find('.select-list').html('');
            },
            rowSelected: function (value) {
                //console.log('value exist :' + existInList(value));
                return objects.list.valueExist(value);
            },
            resetTab: function () {
                element.html('');
                element.myTab();
            }
        };
    };

})(jQuery);


$(document).ready(function () {
    
    $('.myTab').myTab();
    
   /* $('.list-group-item.selectable').click(function (e) {
        selectable_click(this)
    });*/

});