import styled from 'styled-components'
import { palette, breakpoints, typography } from '../../../constants/Styles'

export const Container = styled.section`
  width: 100%;
  display: flex;
  flex-direction: column;

  ${breakpoints.large} {
    flex-direction: row;
    margin-bottom: 2rem;
  }
`
// LeftWrap
export const LeftWrap = styled.section`
  display: flex;
  flex-direction: column-reverse;

  ${breakpoints.large} {
    margin-right: 4rem;
    flex-direction: column;
  }
`

export const MainImgWrap = styled.section`
  margin: 2rem 0;
  display: flex;
  width: 100%;
  justify-content: center;
  align-items: center;
  border-radius: 1rem;

  ${breakpoints.large} {
    margin: 0 0 1rem 0;
  }
`

export const MainImg = styled.img`
  width: 100%;
  height: 30rem;
  border: 1px solid ${palette.divider};
  border-radius: inherit;
  object-fit: cover;

  ${breakpoints.large} {
    margin: 0 0 1rem 0;
    min-width: 40rem;
  }
`

export const ProfileWrap = styled.button`
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 1rem;
  border: 0.1rem solid ${palette.divider};
  box-shadow: 0.1rem 0.5rem 1.1rem ${palette.shadow};
  padding: 1rem 5%;
  cursor: pointer;
`

export const Profile = styled.div`
  display: flex;
  align-items: center;
`
export const ProfileImg = styled.img`
  width: 5rem;
  height: 5rem;
  border-radius: 100%;
  padding: 0.5rem;
  margin-right: 1rem;
  object-fit: cover;
`

export const ProfileId = styled.div`
  font-size: ${typography.mobile.content};

  ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`

// RightWrap
export const RightWrap = styled.div`
  width: 100%;
`
export const Info = styled.div`
  position: relative;
  width: 40rem;
  max-width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 1rem;
  border: 0.1rem solid ${palette.divider};
  box-shadow: 0.1rem 0.5rem 1.1rem ${palette.shadow};
  font-size: ${typography.mobile.desc};
  padding: 1rem 5%;
  font-weight: 600;

  &::after {
    content: '';
    position: absolute;
    left: 50%;
    transform: translate(-50%);
    width: 0.1rem;
    height: 1.5rem;
    background-color: ${palette.divider};
  }
  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`

export const DifficultyInfo = styled.div`
  flex: 1;
  display: flex;
  justify-content: center;
  color: ${palette.main};
`

export const InfoTit = styled.div`
  color: ${palette.textPrimary};
  margin-right: 1rem;
`
export const InfoTxt = styled.div`
  display: flex;
  align-items: center;
  margin-left: 1rem;
`
export const TimeInfo = styled.div`
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
`

export const MainTit = styled.div`
  margin: 3rem 0 1rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: ${typography.mobile.mainTitle};

  & > svg {
    cursor: pointer;
  }

  ${breakpoints.large} {
    font-size: ${typography.web.mainTitle};
  }
`
export const MainTxt = styled.div`
  font-size: ${typography.mobile.content};

  ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`
