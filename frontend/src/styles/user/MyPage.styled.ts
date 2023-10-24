import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
} from '../../constants/Styles'

export const Container = styled.div`
  max-width: 80rem;
  margin: 0 auto;
  padding: 2rem 2rem 10rem;

  ${breakpoints.large} {
    padding: 2rem 2rem;
  }
`

export const ProfileButton = styled.button`
  width: 100%;
  display: flex;
  align-items: center;
`

export const ProfileImage = styled.img`
  width: 5rem;
  height: 5rem;
  border-radius: 2.5rem;
  background-color: gray;

  ${breakpoints.large} {
    width: 10rem;
    height: 10rem;
    border-radius: 5rem;
  }
`

export const PrifileInfo = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-left: 2rem;
`
export const Nickname = styled.span`
  font-size: ${typography.mobile.desc};
  font-weight: ${weight.mainTitle};
  margin-bottom: 0.2rem;
  color: ${palette.textPrimary};

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
    margin-bottom: 0.5rem;
  }
`
export const Email = styled.span`
  font-size: ${typography.mobile.desc};
  color: ${palette.textSecondary};

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`

export const WrapMyPoint = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 1px 1px 1px ${palette.shadow};
  border: 1px solid ${palette.divider};
  border-radius: 1rem;
  padding: 1.5rem 3.5rem;
  margin: 3rem 0;
  font-size: ${typography.mobile.desc};
  font-weight: 800;

  ${breakpoints.large} {
    padding: 3rem 7rem;
    font-size: ${typography.web.desc};
  }
`

export const PointDetailButton = styled.button`
  display: flex;
  align-items: center;
`

export const PointChargeButton = styled.button`
  display: flex;
  align-items: center;
`

export const ChargeBadge = styled.div`
  background-color: ${palette.main};
  padding: 1rem;
  border-radius: 0.5rem;
  font-size: ${typography.mobile.desc};
  color: ${palette.white};
  margin-right: 0.5rem;

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
    padding: 0.5rem 2rem;
    margin-right: 1rem;
  }
`

export const PointIcon = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 1.5rem;
  height: 1.5rem;
  background-color: #ffd542;
  color: ${palette.white};
  border-radius: 0.5rem;
  font-size: ${typography.mobile.desc};
  margin-right: 0.5rem;

  ${breakpoints.large} {
    width: 2rem;
    height: 2rem;
    font-size: ${typography.web.desc};
  }
`

export const ChefBadge = styled.div`
  background-color: ${palette.main};
  color: ${palette.white};
  padding: 0 1rem;
  margin-bottom: 0.5rem;
  border-radius: 0.5rem;
  font-size: ${typography.mobile.desc};

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`

export const NavigateButtonList = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
`

export const NavigateButton = styled.button<{ $isMargin?: boolean }>`
  margin-bottom: ${props => (props.$isMargin ? 3 : 1.5)}rem;
  font-size: ${typography.mobile.desc};

  ${breakpoints.large} {
    margin-bottom: ${props => (props.$isMargin ? 5 : 2)}rem;
    font-size: ${typography.web.desc};
  }
`
