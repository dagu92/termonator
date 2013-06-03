#ifndef TERMONATOR_ICE
#define TERMONATOR_ICE
module utils {
  interface Controlador {
  
  };
  interface Caldera {
    bool encender();
    bool apagar();
    void anadirCaldera(utils::Controlador* proxy);
  };
};
#endif
