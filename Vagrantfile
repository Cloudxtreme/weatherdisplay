# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure(2) do |config|
  config.vm.box = "box-cutter/ubuntu1404-desktop"
  config.vm.hostname = "weather"
  
  config.vm.synced_folder ".", "/vagrant"
  
  config.vm.provider "virtualbox" do |vb|
    vb.name = "weather"
    vb.gui = true
    vb.memory = "1024"
    vb.customize [
    	"modifyvm", :id,
    	"--paravirtprovider", "kvm",
    ]
  end

  config.vm.provision "ansible" do |ansible|
    ansible.playbook = "ansible/playbook.yml"
    ansible.groups = {
      "vagrant" => ["default"]
    }
    #ansible.verbose = "vvv"
  end
end
