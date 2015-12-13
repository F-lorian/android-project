<?php

/* index.html.twig */
class __TwigTemplate_b6d061bd6db764aebe393d9d396873d1439fee4c2378435310b8ecfcacf9bfe0 extends Twig_Template
{
    public function __construct(Twig_Environment $env)
    {
        parent::__construct($env);

        $this->parent = false;

        $this->blocks = array(
        );
    }

    protected function doDisplay(array $context, array $blocks = array())
    {
        // line 1
        echo "<!DOCTYPE html>
<html>

<head>
    <meta charset=\"UTF-8\" />
    <title>Sign project</title>

    <link rel=\"stylesheet\" href=\"css/bootstrap.min.css\" type=\"text/css\" />
    <link rel=\"stylesheet\" href=\"css/styles.css\" type=\"text/css\" />
    <link rel=\"stylesheet\" href=\"css/listgroup.css\" type=\"text/css\" />
    <link rel=\"stylesheet\" href=\"css/label.css\" type=\"text/css\" />
    <link rel=\"stylesheet\" href=\"css/pagination.css\" type=\"text/css\" />

    <link rel=\"icon\" type=\"image/x-icon\" href=\"\" />
</head>

<body>

    <div id=\"global-container\" class=\"container-fluid\">
        
        <h4>Sign Project</h4>
        <div id=\"users\" class=\"list-group\">
            <a class=\"list-group-item list-group-action\" href=\"\">
                <span class=\"label label-primary inset\">
                    <i class=\"glyphicon glyphicon-plus\"></i>
                </span> envoyer une notification
            </a>
            <div class=\"list-group-item list-group-title\" >
                <div class=\"row\">
                    <div class=\"col-sm-4\">
                        pseudo
                    </div>
                    <div class=\"col-sm-4\">
                        mail
                    </div>
                    <div class=\"col-sm-4\">
                        registrationID
                    </div>
                </div>
            </div>
            ";
        // line 41
        $context['_parent'] = $context;
        $context['_seq'] = twig_ensure_traversable((isset($context["users"]) ? $context["users"] : null));
        foreach ($context['_seq'] as $context["_key"] => $context["u"]) {
            // line 42
            echo "            <a class=\"list-group-item active\" >
                <div class=\"row\">
                    <div class=\"col-sm-4\">
                        <span class=\"label label-primary inset\">
                            <i class=\"glyphicon glyphicon-user\"></i>
                        </span> ";
            // line 47
            echo twig_escape_filter($this->env, $this->getAttribute($context["u"], "pseudo", array()), "html", null, true);
            echo "
                    </div>
                    <div class=\"col-sm-4\">
                        ";
            // line 50
            echo twig_escape_filter($this->env, $this->getAttribute($context["u"], "email", array()), "html", null, true);
            echo "
                    </div>
                    <div class=\"col-sm-4\">
                        ";
            // line 53
            echo twig_escape_filter($this->env, $this->getAttribute($context["u"], "gcm_regid", array()), "html", null, true);
            echo "
                    </div>
                </div>
            </a>

            <a class=\"list-group-item\" href=\"\">
                <div class=\"row\">
                    <div class=\"col-sm-4\">
                        <span class=\"label label-default inset\">
                            <i class=\"glyphicon glyphicon-user\"></i>
                        </span> ";
            // line 63
            echo twig_escape_filter($this->env, $this->getAttribute($context["u"], "pseudo", array()), "html", null, true);
            echo "
                    </div>
                    <div class=\"col-sm-4\">
                        ";
            // line 66
            echo twig_escape_filter($this->env, $this->getAttribute($context["u"], "email", array()), "html", null, true);
            echo "
                    </div>
                    <div class=\"col-sm-4\">
                
                    </div>
                </div>
            </a>
            ";
        }
        $_parent = $context['_parent'];
        unset($context['_seq'], $context['_iterated'], $context['_key'], $context['u'], $context['_parent'], $context['loop']);
        $context = array_intersect_key($context, $_parent) + $_parent;
        // line 74
        echo "        </div>
    </div>
    
    <form method=\"post\" action=\"\">

        <input type=\"submit\" value=\"test\"></input>

    </form>
    
    <script src=\"\"></script>
</body>

</html>";
    }

    public function getTemplateName()
    {
        return "index.html.twig";
    }

    public function isTraitable()
    {
        return false;
    }

    public function getDebugInfo()
    {
        return array (  117 => 74,  103 => 66,  97 => 63,  84 => 53,  78 => 50,  72 => 47,  65 => 42,  61 => 41,  19 => 1,);
    }
}
/* <!DOCTYPE html>*/
/* <html>*/
/* */
/* <head>*/
/*     <meta charset="UTF-8" />*/
/*     <title>Sign project</title>*/
/* */
/*     <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />*/
/*     <link rel="stylesheet" href="css/styles.css" type="text/css" />*/
/*     <link rel="stylesheet" href="css/listgroup.css" type="text/css" />*/
/*     <link rel="stylesheet" href="css/label.css" type="text/css" />*/
/*     <link rel="stylesheet" href="css/pagination.css" type="text/css" />*/
/* */
/*     <link rel="icon" type="image/x-icon" href="" />*/
/* </head>*/
/* */
/* <body>*/
/* */
/*     <div id="global-container" class="container-fluid">*/
/*         */
/*         <h4>Sign Project</h4>*/
/*         <div id="users" class="list-group">*/
/*             <a class="list-group-item list-group-action" href="">*/
/*                 <span class="label label-primary inset">*/
/*                     <i class="glyphicon glyphicon-plus"></i>*/
/*                 </span> envoyer une notification*/
/*             </a>*/
/*             <div class="list-group-item list-group-title" >*/
/*                 <div class="row">*/
/*                     <div class="col-sm-4">*/
/*                         pseudo*/
/*                     </div>*/
/*                     <div class="col-sm-4">*/
/*                         mail*/
/*                     </div>*/
/*                     <div class="col-sm-4">*/
/*                         registrationID*/
/*                     </div>*/
/*                 </div>*/
/*             </div>*/
/*             {% for u in users %}*/
/*             <a class="list-group-item active" >*/
/*                 <div class="row">*/
/*                     <div class="col-sm-4">*/
/*                         <span class="label label-primary inset">*/
/*                             <i class="glyphicon glyphicon-user"></i>*/
/*                         </span> {{u.pseudo}}*/
/*                     </div>*/
/*                     <div class="col-sm-4">*/
/*                         {{u.email}}*/
/*                     </div>*/
/*                     <div class="col-sm-4">*/
/*                         {{u.gcm_regid}}*/
/*                     </div>*/
/*                 </div>*/
/*             </a>*/
/* */
/*             <a class="list-group-item" href="">*/
/*                 <div class="row">*/
/*                     <div class="col-sm-4">*/
/*                         <span class="label label-default inset">*/
/*                             <i class="glyphicon glyphicon-user"></i>*/
/*                         </span> {{u.pseudo}}*/
/*                     </div>*/
/*                     <div class="col-sm-4">*/
/*                         {{u.email}}*/
/*                     </div>*/
/*                     <div class="col-sm-4">*/
/*                 */
/*                     </div>*/
/*                 </div>*/
/*             </a>*/
/*             {% endfor %}*/
/*         </div>*/
/*     </div>*/
/*     */
/*     <form method="post" action="">*/
/* */
/*         <input type="submit" value="test"></input>*/
/* */
/*     </form>*/
/*     */
/*     <script src=""></script>*/
/* </body>*/
/* */
/* </html>*/
