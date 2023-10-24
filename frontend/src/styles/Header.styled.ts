import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
  zindex,
} from '../constants/Styles'

export const Container = styled.header`
  position: sticky;
  top: 0;
  max-height: 9rem;
  height: 9rem;
  padding: 5%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: ${palette.white};
  z-index: ${zindex.header};

  ${breakpoints.large} {
    padding: 2%;
  }
`

export const LogoButton = styled.button`
  width: 30%;
`

export const Logo = styled.img`
  width: 100%;
  height: auto;

  ${breakpoints.large} {
    width: 50%;
  }
`

export const WrapMenu = styled.ul`
  display: none;
  align-items: center;
  padding-right: 5rem;

  ${breakpoints.large} {
    display: flex;
  }
`

export const Menu = styled.button`
  padding: 0px 1.8rem;
  font-size: ${typography.web.content};
  font-weight: ${weight.subTitle};
  color: ${palette.textPrimary};
`

export const WrapIcon = styled.ul`
  display: flex;
  padding-right: 5rem;

  ${breakpoints.large} {
    display: none;
  }
`

export const Icon = styled.button`
  margin-left: 1rem;
`

export const LoginButton = styled.button`
  padding: 0.8rem 1.2rem;
  background-color: ${palette.main};
  color: white;
  border-radius: 0.5rem;
  margin-left: 1rem;
  font-size: ${typography.mobile.desc};
  font-weight: 600;

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`

export const Button = styled.button``
