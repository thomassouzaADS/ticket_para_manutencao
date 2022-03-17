(function () {
  'use strict'
  // Pega todos os formulários que desejamos aplicar o estilo do Bootstrap
  var forms = document.querySelectorAll('.needs-validation')
  // Impede o envio imediato do formulário
  Array.prototype.slice.call(forms)
    .forEach(function (form) {
      form.addEventListener('submit', function (event) {
        if (!form.checkValidity()) {
          event.preventDefault()
          event.stopPropagation()
        }
        form.classList.add('was-validated')
      }, false)
    })
})()
// Copiar essas funções
//https://pt.stackoverflow.com/questions/53275/m%c3%a1scaras-para-telefone-e-cpf-em-textfield
function mascara(objeto,funcao){
    v_obj=objeto
    v_fun=funcao
    setTimeout("execmascara()",1);
}

function execmascara(){
    v_obj.value=v_fun(v_obj.value);
}

function id( objeto ){
	return document.getElementById( objeto );
}

function mtel(v){ //MASCARA PARA TELEFONE

    v=v.replace(/\D/g,"");             //Remove tudo o que não é dígito
    v=v.replace(/^(\d{2})(\d)/g,"($1) $2"); //Coloca parênteses em volta dos dois primeiros dígitos
    v=v.replace(/(\d)(\d{4})$/,"$1-$2");    //Coloca hífen entre o quarto e o quinto dígitos
    return v;
}

function mdata(v){ // MASCARA PARA DATA

    v=v.replace(/\D/g,"");                    //Remove tudo o que não é dígito
    v=v.replace(/(\d{2})(\d)/,"$1/$2");
    v=v.replace(/(\d{2})(\d)/,"$1/$2");
    return v;
}

function memail(v){

		v=v.replace( /\s/g, '' );
        return v;
}