# Weatherdisplay

This project uses Selenium to remote-control a Chrome instance to display certain
pages from The Weather Channel web page.

# Deployment Scenarios

## Sandbox with Vagrant

The Vagrantfile defines a single box that can be used to test the setup.  Running
`vagrant up` will bring up an Ubuntu 14.04 VM and deploy the Groovy script and
lightdm configuration.

## Hardware

Running ansible against a read machine, for example an Intel Compute Stick with Ubuntu:

    ansible-playbook -i ansible/inventory ansible/playbook.yml


# Project Structure

The Ansible configuration is split into a Ubuntu-specific role `ubuntu`, and the main 
`weather-kiosk` role.

## Role ubuntu

This role removes superflous packages, updates all installed packages, and installs
necessary packages to run the machine in kiosk mode.

It downloads chromedriver version 2.20 and installs in in /usr/local/bin on the target.


## Role weather-kiosk

This role installs a custom lightdm configuration, creates a user `kiosk`, and populates
that user's home directory with all files necessary. Finally, lightdm is restarted so
that the kiosk user is logged in automatically.

`~kiosk/.xsession` (`ansible/roles/files/dotxinitrc`) contains all commands to start up
a session for the kiosk user.

`ansible/roles/files/kiosk/kiosk.sh` runs the main Groovy script in a loop, cleaning up
any left-over processes after the Groovy script finishes.


# Selenium Groovy Script

`ansible/roles/files/kiosk/weatherchannel.groovy` starts up chromedriver and loads
pages from TWC.

The script can be customized to a specific location: near the top of the script, the
`location` variable should be set to the location ID for which you'd like to receive
weather information. 

The Groovy script can be run on the command line on a local machine for development
purposes; in this case, call it with `dev` to configure Chrome not to run in kiosk mode.

    groovy weatherchannel.groovy dev

