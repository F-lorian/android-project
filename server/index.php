<?php
if(isset($isLogged) && $isLogged){
?>

<div id="panel-users" class="panel visible">
    <div id="result-message"></div>
    <div class="panel-header">
        <span class="label label-default">Sélection</span>
    </div>
    <div class="panel-body">
        <div id="user_list" class="myList">
        </div>
    </div>
    <form id="notification" onSubmit="return sendNotification()">
        <textarea name="message" id="message" class="form-control" placeholder="message..."></textarea>
        <button class="form-control list-group-item list-group-action">
            <span class="label label-primary inset">
                    <i class="glyphicon glyphicon-send"></i>
                </span> envoyer
        </button>
    </form>
</div>

<div id="global-container" class="container-fluid">

    <h4>Sign Project</h4>


    <div class="panel">
        <div class="panel-header">
            <span class="label label-default">Recherche</span>
        </div>
        <div class="panel-body">
            <form id="search" onSubmit="return getUsers()">
                <div class="input-group">
                    <input type="text" class="form-control" id="search-user" name="search-user" placeholder="chercher utilisateur">
                    <span class="input-group-btn">
                            <button class="btn btn-primary" type="submit"><i class="glyphicon glyphicon-search"></i></button>
                        </span>
                </div>
            </form>
            <?php
               // echo testNotification();
               /* echo addToGroup(74, 15, 'attente');
                echo isInGroup(15, 74);
    echo isInGroup(8, 82);*/
               // print_r(getGroups(0, ''));
            ?>
        </div>
    </div>


    <div id="users" class="mytab">
        <div class="list-group">
            <!--<a class="list-group-item list-group-action" onclick="">
                <span class="label label-primary inset">
                        <i class="glyphicon glyphicon-plus"></i>
                    </span> envoyer une notification
            </a>
            <div class="list-group-item list-group-title">
                <div class="row">
                    <div class="col-sm-4">
                        pseudo
                    </div>
                    <div class="col-sm-4">
                        mail
                    </div>
                    <div class="col-sm-4">
                        registrationID
                    </div>
                </div>
            </div>
            <span class="select-list">
            </span>-->
        </div>
    </div>
</div>

<?php
}
?>

